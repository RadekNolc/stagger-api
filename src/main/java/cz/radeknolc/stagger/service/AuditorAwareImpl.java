package cz.radeknolc.stagger.service;

import cz.radeknolc.stagger.model.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        if (UserDetailsImpl.getLoggedUser() != null) {
            return Optional.ofNullable(UserDetailsImpl.getLoggedUser().getUsername());
        }

        return Optional.empty();
    }
}
