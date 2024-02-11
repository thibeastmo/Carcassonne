package be.kdg.backend_game.service;

import be.kdg.backend_game.repository.HelloWorldRepository;
import be.kdg.backend_game.service.dto.HelloWorldResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HelloWorldService {
    private final HelloWorldRepository helloWorldRepository;

    public HelloWorldService(HelloWorldRepository helloWorldRepository) {
        this.helloWorldRepository = helloWorldRepository;
    }

    public Optional<HelloWorldResponseDto> getHelloWorld() {
        Optional<String> helloValue =  Optional.ofNullable(helloWorldRepository.findAll().get(0).getHelloValue());
        if (helloValue.isPresent()){
            HelloWorldResponseDto response = new HelloWorldResponseDto();
            response.setHelloWorld(helloValue.get());
            return Optional.of(response);
        }
        return Optional.empty();
    }
}
