package courses.monsters.tezos.controllers;

import courses.monsters.tezos.services.MonsterFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class MonsterFactoryController {

    @Autowired
    private MonsterFactoryService monsterFactoryService;

    @GetMapping(value = "/{code}/", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getMonster(@PathVariable(value = "code") String code)
            throws IOException {
        return monsterFactoryService.getImage(code);
    }


}
