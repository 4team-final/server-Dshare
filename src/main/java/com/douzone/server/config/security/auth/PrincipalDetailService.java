package com.douzone.server.config.security.auth;

import com.douzone.server.entity.Employee;
import com.douzone.server.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrincipalDetailService implements UserDetailsService {
	private static final String METHOD_NAME = "PrincipalDetailService";
	private final EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info(METHOD_NAME + "- loadUserByUsername() ...");
		Employee employee = employeeRepository.findByEmpNo(username);

		if (employee == null) {
			log.error("유저가 존재하지 않습니다. " + METHOD_NAME);
		}
		return new PrincipalDetails(employee);
	}
}
