package org.example.finalprojecttuwaiq.Config;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class ConfigSecurity {
    private final MyUserDetailsService myUserDetailsService;
    private final JwtAuthenticationFilter jwtFilter;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http.csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .authenticationProvider(daoAuthenticationProvider())
               .authorizeHttpRequests()

//               //Business Analyst ROUTES
               .requestMatchers("/api/v1/ba/register").permitAll()
//               .requestMatchers("/api/v1/employee/update/{employee_id}").hasAnyAuthority("ADMIN","EMPLOYEE")
//               .requestMatchers("/api/v1/employee/delete/{employee_id}").hasAnyAuthority("ADMIN","EMPLOYEE")
//
//
//                //Stakeholder Routes
               .requestMatchers("/api/v1/customer/register").permitAll()
//               .requestMatchers("/api/v1/customer/get").hasAuthority("ADMIN")
//               .requestMatchers("/api/v1/customer/delete/{customer_id}").hasAnyAuthority("CUSTOMER","ADMIN","EMPLOYEE")
//               .requestMatchers("/api/v1/customer/update/{customer_id}").hasAnyAuthority("CUSTOMER","ADMIN","EMPLOYEE")
//
//
//               //ACCOUNT ROUTES
//               .requestMatchers("/api/v1/account/get").hasAuthority("ADMIN")
//               .requestMatchers("/api/v1/account/create").hasAuthority("CUSTOMER")
//               .requestMatchers("/api/v1/account/my-accounts").hasAuthority("CUSTOMER")
//               .requestMatchers("/api/v1/account/update/{account_id}").hasAnyAuthority("CUSTOMER","ADMIN")
//               .requestMatchers("/api/v1/account/delete/{account_id}").hasAnyAuthority("CUSTOMER","ADMIN")
//               .requestMatchers("/api/v1/account/{account_id}/activate").hasAnyAuthority("ADMIN","EMPLOYEE")
//               .requestMatchers("/api/v1/account/{account_id}").hasAuthority("CUSTOMER")
//               .requestMatchers("/api/v1/account/{account_id}/deposit/{amount}").hasAuthority("CUSTOMER")
//               .requestMatchers("/api/v1/account/{account_id}/withdraw/{amount}").hasAuthority("CUSTOMER")
//               .requestMatchers("/api/v1/account/transfer/{from_account_id}/{to_account_id}/{amount}").hasAuthority("CUSTOMER")
//               .requestMatchers("/api/v1/account/block/{account_id}").hasAnyAuthority("ADMIN","EMPLOYEE")
//
//
//               //ADMIN ROUTES
//               .requestMatchers("/api/v1/auth/register").hasAuthority("ADMIN")
//               .requestMatchers("/api/v1/auth/get/users").hasAuthority("ADMIN")


               //Login
               .requestMatchers("/api/v1/auth/login").permitAll()
               .anyRequest().authenticated()
               .and()
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();

    }


}
