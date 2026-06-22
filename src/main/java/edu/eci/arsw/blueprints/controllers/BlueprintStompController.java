package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.dto.BlueprintUpdate;
import edu.eci.arsw.blueprints.dto.DrawEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BlueprintStompController {

    private final SimpMessagingTemplate template;

    public BlueprintStompController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/draw")
    public void onDraw(DrawEvent evt) {
        var upd = new BlueprintUpdate(
                evt.author(),
                evt.name(),
                List.of(evt.point())
        );

        template.convertAndSend(
                "/topic/blueprints." + evt.author() + "." + evt.name(),
                upd
        );
    }
}