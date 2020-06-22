package courses.monsters.tezos.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString
public class Monster {

    public Monster(int body_id) {
        this.body_id = body_id;
        this.parts = new HashMap<>();
    }

    @Getter
    @Setter
    private int body_id;

    @Getter
    @Setter
    private Map<String, MonsterPart> parts;

}

