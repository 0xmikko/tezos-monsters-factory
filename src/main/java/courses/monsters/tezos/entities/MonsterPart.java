package courses.monsters.tezos.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
public class MonsterPart {

    public MonsterPart(int body, String part, int id) {
        switch (part) {
            case "hat":
                create(body, part, id, 160, 0);
                break;
            case "eyes":
                create(body, part, id, 160, 500);
                break;
            case "nose":
                create(body, part, id, 160, 650);
                break;
            case "mouth":
                create(body, part, id, 160, 720);
                break;
        }

    }

    private void create(int body, String part, int id, int x, int y) {
        this.body = body;
        this.part = part;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Getter
    @Setter
    private int body;

    @Getter
    @Setter
    private String part;

    @Getter
    @Setter
    private int id = 1;

    @Getter
    @Setter
    private int x = 0;

    @Getter
    @Setter
    private int y = 0;

}