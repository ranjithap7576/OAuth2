package security.oauth.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by nydiarra on 06/05/17.
 */
@Component
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	Map<String, User> map = new HashMap<>();

	@PostConstruct
	public void init() {
		// Long id, String username, String password, String firstName, String lastName,
		// List<Role> roles
		List<Role> roles = new ArrayList<>(1);
		roles.add(new Role(1L, "STANDARD_USER", "Standard User"));
		User john = new User(1L, "john.doe", "821f498d827d4edad2ed0960408a98edceb661d9f34287ceda2962417881231a", "John", "Doe", roles);

		List<Role> roles2 = new ArrayList<>(1);
		roles2.add(new Role(1L, "ADMIN_USER", "Admin User"));
		roles2.add(new Role(2L, "STANDARD_USER", "Standard User"));
		User admin = new User(1L, "admin.admin", "821f498d827d4edad2ed0960408a98edceb661d9f34287ceda2962417881231a", "Admin", "Admin", roles2);
		map.put("john.doe", john);
		map.put("admin.admin", admin);
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//		User user = userRepository.findByUsername(s);
		User user = map.get(s);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		});

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), authorities);

		return userDetails;
	}
}