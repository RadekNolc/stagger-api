package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private List<String> roles;
    private int coins;
    private int maxCoins;
    //private double successRate;
    private int universityCount;

    public UserProfileResponse(User user) {
        roles = user.getRoles().stream().map(role -> role.getRole().getAuthority()).collect(Collectors.toList());
        coins = user.getCoins();
        maxCoins = user.getMaxCoins();
        //successRate = 100;
        universityCount = user.getUniversities().size();
    }

    public static List<UserProfileResponse> parseList(List<User> users) {
        List<UserProfileResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserProfileResponse(user));
        }

        return result;
    }
}
