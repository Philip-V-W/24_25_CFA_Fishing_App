package cfa.fishing.fishing_store_app.service.account;

import cfa.fishing.fishing_store_app.dto.request.AddressRequest;
import cfa.fishing.fishing_store_app.dto.request.UserProfileUpdateRequest;
import cfa.fishing.fishing_store_app.dto.response.*;
import cfa.fishing.fishing_store_app.entity.contest.ContestRegistration;
import cfa.fishing.fishing_store_app.entity.order.Order;
import cfa.fishing.fishing_store_app.entity.permit.FishingPermit;
import cfa.fishing.fishing_store_app.entity.user.Address;
import cfa.fishing.fishing_store_app.entity.user.User;
import cfa.fishing.fishing_store_app.entity.permit.PermitStatus;
import cfa.fishing.fishing_store_app.exception.ResourceNotFoundException;
import cfa.fishing.fishing_store_app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final PermitRepository permitRepository;
    private final ContestRepository contestRepository;

    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToUserProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(String email, UserProfileUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        return mapToUserProfileResponse(userRepository.save(user));
    }

    public List<AddressResponse> getAddresses(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getAddresses().stream()
                .map(this::mapToAddressResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse addAddress(String email, AddressRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = new Address();
        address.setUser(user);
        address.setStreetAddress(request.getStreetAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        if (request.isDefault() || !addressRepository.existsByUserAndIsDefaultTrue(user)) {
            addressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(defaultAddress -> defaultAddress.setDefault(false));
            address.setDefault(true);
        }

        return mapToAddressResponse(addressRepository.save(address));
    }

    @Transactional
    public AddressResponse updateAddress(String email, Long addressId, AddressRequest request) {
        Address address = getAddressForUser(email, addressId);

        address.setStreetAddress(request.getStreetAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        if (request.isDefault() && !address.isDefault()) {
            addressRepository.findByUserAndIsDefaultTrue(address.getUser())
                    .ifPresent(defaultAddress -> defaultAddress.setDefault(false));
            address.setDefault(true);
        }

        return mapToAddressResponse(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(String email, Long addressId) {
        Address address = getAddressForUser(email, addressId);

        if (address.isDefault()) {
            address.getUser().getAddresses().stream()
                    .filter(a -> !a.getId().equals(addressId))
                    .findFirst()
                    .ifPresent(newDefault -> {
                        newDefault.setDefault(true);
                        addressRepository.save(newDefault);
                    });
        }

        addressRepository.delete(address);
    }

    @Transactional
    public void setDefaultAddress(String email, Long addressId) {
        Address newDefault = getAddressForUser(email, addressId);
        User user = newDefault.getUser();

        addressRepository.findByUserAndIsDefaultTrue(user)
                .ifPresent(currentDefault -> currentDefault.setDefault(false));

        newDefault.setDefault(true);
        addressRepository.save(newDefault);
    }

    private Address getAddressForUser(String email, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Not authorized to access this address");
        }

        return address;
    }

    private UserProfileResponse mapToUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .registrationDate(user.getCreatedAt())
                .addresses(user.getAddresses().stream()
                        .map(this::mapToAddressResponse)
                        .collect(Collectors.toList()))
                .recentOrders(orderRepository.findByUserOrderByOrderDateDesc(user).stream()
                        .limit(5)
                        .map(this::mapToOrderSummaryResponse)
                        .collect(Collectors.toList()))
                .activePermits(permitRepository.findByUserAndStatus(user, PermitStatus.APPROVED).stream()
                        .map(this::mapToPermitSummaryResponse)
                        .collect(Collectors.toList()))
                .upcomingContests(contestRepository.findUserUpcomingContests(user.getId()).stream()
                        .map(this::mapToContestRegistrationResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private AddressResponse mapToAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .streetAddress(address.getStreetAddress())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .isDefault(address.isDefault())
                .build();
    }

    private OrderSummaryResponse mapToOrderSummaryResponse(Order order) {
        return OrderSummaryResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .trackingNumber(order.getTrackingNumber())
                .itemCount(order.getItems().size())
                .build();
    }

    private PermitSummaryResponse mapToPermitSummaryResponse(FishingPermit permit) {
        return PermitSummaryResponse.builder()
                .id(permit.getId())
                .permitNumber(permit.getPermitNumber())
                .type(permit.getPermitType())
                .startDate(permit.getStartDate())
                .endDate(permit.getEndDate())
                .status(permit.getStatus())
                .price(permit.getPrice())
                .build();
    }

    private ContestRegistrationResponse mapToContestRegistrationResponse(ContestRegistration registration) {
        return ContestRegistrationResponse.builder()
                .id(registration.getId())
                .contestId(registration.getContest().getId())
                .contestName(registration.getContest().getName())
                .contestDate(registration.getContest().getStartDate())
                .location(registration.getContest().getLocation())
                .registrationDate(registration.getRegistrationDate())
                .entryFee(registration.getContest().getEntryFee())
                .status(registration.getStatus().name())
                .build();
    }
}
