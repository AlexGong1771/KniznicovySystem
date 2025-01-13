//package com.example.kniznica.config;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/deleteUser**").hasRole("ADMIN")
//                        .requestMatchers("/users/**").hasRole("ADMIN")
//                        .requestMatchers("/roles/**").hasRole("ADMIN")
//                        .requestMatchers("/zmazNapredovanie**").hasRole("ADMIN")
//                        .requestMatchers("/odborky/save**").hasRole("ADMIN")
//                        .requestMatchers("/odborky/addPart**").hasRole("ADMIN")
//                        .requestMatchers("/odborky/zmazUlohu**").hasRole("ADMIN")
//                        .requestMatchers("/odborky/upravNapredovanie**").hasRole("ADMIN")
//                        .requestMatchers("/editUser**").authenticated()
//                        .requestMatchers("/editUserPasswd**").authenticated()
//                        .requestMatchers("/odborky/zacniPlnit/**").authenticated()
//                        .requestMatchers("/odborky/aktualizujStavUlohy/**").authenticated()
//                        .requestMatchers("/odborky/mojeNapredovanie/**").authenticated()
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/register/**").permitAll()
//                        .requestMatchers("/images/**").permitAll() //loga v resources
//                        .requestMatchers("/img/**").permitAll() //nahrate obrazky mapovene cez WebConfig
//                        .requestMatchers("/css/**").permitAll()
//                        .requestMatchers("/JavaSctipt/**").permitAll()
//                        .requestMatchers("/api/**").permitAll()
//                        .requestMatchers("/index").permitAll()
//                        .requestMatchers("/odborky/**").permitAll()
//                        .requestMatchers("/vyzvy").permitAll()
//                        .requestMatchers("/volne-programove-moduly").permitAll()
//                        .requestMatchers("/kontakt").permitAll()
//                        .requestMatchers("/about").permitAll()
//                        .requestMatchers("/").permitAll()
//                ).formLogin(
//                        form -> form
//                                .loginPage("/login")
//                                .loginProcessingUrl("/login")
//                                .defaultSuccessUrl("/index", true)
//                                .permitAll()
//                ).logout(
//                        logout -> logout
//                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                                .invalidateHttpSession(true)
//                                .deleteCookies("JSESSIONID")
//                                .permitAll()
//                ).exceptionHandling(exception -> exception
//                        //tu presmeruj ak by si mal vratit status 403
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
//                                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // vratenie statusu 403 pre AJAX požiadavky
//                                response.getWriter().write("Access Denied"); // chybova sprava v odpovedi
//                            } else {
//                                response.sendRedirect("/index"); // inak presmeruj tu
//                            }
//                        })
//                );
//        return http.build();
//    }
//
//}
