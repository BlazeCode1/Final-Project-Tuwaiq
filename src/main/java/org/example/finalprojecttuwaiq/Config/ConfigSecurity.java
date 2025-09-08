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
               .requestMatchers("/api/v1/ba/projects").hasAuthority("BA")
               .requestMatchers("/api/v1/ba/update").hasAuthority("BA")
               .requestMatchers("/api/v1/ba/delete").hasAuthority("BA")
//
//
//                //Stakeholder Routes
               .requestMatchers("/api/v1/stakeholder/register").permitAll()
               .requestMatchers("/api/v1/stakeholder/update").hasAuthority("STAKEHOLDER")
               .requestMatchers("api/v1/stakeholder/delete").hasAuthority("STAKEHOLDER")
//
//
//               //Approval ROUTES
               .requestMatchers("/api/v1/approvals/approve").hasAuthority("STAKEHOLDER")
               .requestMatchers("/api/v1/approvals/pending").hasAuthority("STAKEHOLDER")
               .requestMatchers("/api/v1/approvals/reject").hasAuthority("STAKEHOLDER")
               .requestMatchers("/api/v1/approvals/send").hasAuthority("BA")
//
//               //Document ROUTES
               .requestMatchers("/api/v1/documents/generate/**").hasAuthority("BA")

//               //Diagram ROUTES
               .requestMatchers("/api/v1/diagrams/generate/**").hasAuthority("BA")

               //Payment ROUTES
               .requestMatchers("/api/v1/payment/monthly").hasAuthority("BA")
               .requestMatchers("/api/v1/payment/yearly").hasAuthority("BA")
               .requestMatchers("/api/v1/payment/subscription/status").hasAuthority("BA")
               .requestMatchers("/api/v1/payment/subscription/cancel").hasAuthority("BA")
               .requestMatchers("/api/v1/payment/callback/**").permitAll()

               // Jira ROUTES
               .requestMatchers("/api/v1/jira/issue-from-userstory/{userStoryId}").hasAuthority("BA")
               .requestMatchers("/api/v1/jira/issue-from-project/{project_id}").hasAuthority("BA")

                // Project ROUTES
               .requestMatchers("/api/v1/projects/add").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/get/{id}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/update/{id}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/delete/{id}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/{projectId}/market-benchmark").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/assign/stakeholder/{stakeholder_id}/{project_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/assign/ba/{ba_id}/{project_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/recommend-tools/{projectId}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/reassign/owner/{project_id}/{nextOwnerId}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/exit/ba/{project_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/projects/exit/stakeholder/{project_id}").hasAuthority("STAKEHOLDER")

               //Requirements ROUTES
               .requestMatchers("/api/v1/requirement/add").hasAuthority("BA")
               .requestMatchers("/api/v1/requirement/by-project/{project_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/requirement/generate/{project_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/requirement/draft/get/{draft_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/requirement/draft/accept/{draft_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/requirement/draft/reject/{draft_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/requirement/update/{id}").hasAuthority("BA")
               .requestMatchers("/api/v1/requirement/delete/{id}").hasAuthority("BA")

               //UserStory ROUTES
               .requestMatchers("/api/v1/user-stories/add").hasAuthority("BA")
               .requestMatchers("/api/v1/user-stories/update/{id}").hasAuthority("BA")
               .requestMatchers("/api/v1/user-stories/delete/{id}").hasAuthority("BA")
               .requestMatchers("/api/v1/user-stories/generate/{requirement_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/user-stories/draft/requirement/{requirement_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/user-stories/draft/get/{draft_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/user-stories/draft/accept/{draft_id}").hasAuthority("BA")
               .requestMatchers("/api/v1/user-stories/draft/reject/{draft_id}").hasAuthority("BA")

               //Login
               .requestMatchers("/api/v1/auth/login").permitAll()

               //Admin ROUTES
               .requestMatchers("/api/v1/users/**").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/approvals/get").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/approvals/get/{id}").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/projects/get").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/ba/get").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/stakeholder/get").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/diagrams/get").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/user-stories/get").hasAuthority("ADMIN")
               .requestMatchers("/api/v1/requirement/get").hasAuthority("ADMIN")
               .anyRequest().authenticated()
               .and()
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();

    }


}
