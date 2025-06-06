//package jrainroot.still_home.controller;
//
//import ch.qos.logback.core.model.Model;
//import lombok.AllArgsConstructor;
//import org.hibernate.mapping.Resolvable;
//import org.springframework.core.ResolvableType;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@AllArgsConstructor
//public class BaseController {
//
//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }
//
//    @GetMapping("/user")
//    public String user() {
//        return "user";
//    }
//
//    private static final String authorizationRequestBaseUri = "oauth2/authorization";
//    private final ClientRegistrationRepository clientRegistrationRepository;
//    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
//
//    @GetMapping("/login")
//    public String getLoginPage(Model model) throws Exception {
//        Iterable<ClientRegistration> clientRegistrations = null;
//        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
//                .as(Iterable.class);
//        if (type != ResolvableType.NONE &&
//            ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
//            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
//        }
//
//        assert clientRegistrations != null;
//        clientRegistrations.forEach(registration ->
//                oauth2AuthenticationUrls.put(registration.getRegistrationId(),
//                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
//
//        return "login";
//    }
//
//    @RequestMapping("/accessDenied")
//    public String accessDenied() { return "accessDenied"; }
//}
