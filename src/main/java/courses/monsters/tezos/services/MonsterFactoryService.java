package courses.monsters.tezos.services;

import courses.monsters.tezos.entities.Monster;
import courses.monsters.tezos.entities.MonsterPart;
import courses.monsters.tezos.exceptions.InvalidMonsterCodeException;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@ToString
public class MonsterFactoryService {

    private String[] parts = {"hat", "eyes", "nose", "mouth"};
    private int maxBody;
    private Map<String, Integer> maxItem;

    @Value("${tezosmonsters.factory.pictures_folder}")
    private String path;


    @PostConstruct
    public void init() {

        maxItem = new HashMap<>();
        try {
            File file = ResourceUtils.getFile(path);
            System.out.println("Looking pictures in " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println(e);
        }


        maxBody = getMaxImageNumber("body");
        Arrays.stream(parts).forEach(p -> maxItem.put(p, getMaxImageNumber(p)));
        System.out.println(this.toString());
    }

    public byte[] getImage(String code) throws IOException, InvalidMonsterCodeException {
        var monster = getMonster(code);
        System.out.println(monster);
        return getImage(monster);
    }

    public byte[] getImage(Monster monster) throws IOException {

        // load body image
        BufferedImage body = ImageIO.read(new File(path + "/body_" + monster.getBody_id() + ".png"));
        // create the new image, canvas size is the max. of both image sizes
        int w = body.getWidth();
        int h = body.getHeight() + 200;
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.drawImage(body, 0, 200, null);

        monster.getParts().entrySet().forEach(p -> {
            try {
                var monsterPart = p.getValue();
                BufferedImage partImage = ImageIO.read(new File(path + "/" + p.getKey() + "_" + monsterPart.getId() + ".png"));
                g.drawImage(partImage, monsterPart.getX(), monsterPart.getY(), null);
            } catch (IOException e) {
                System.out.println("Problem with " + p);
            }
        });


        g.dispose();

        // Save as new image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(result, "png", baos);
        return baos.toByteArray();
    }

    private int getMaxImageNumber(String part) {
        int result = 1;
        try {
            while (true) {
                File file = new File(path + "/" + part + "_" + result + ".png");
                if (!file.canRead()) {
                    throw new Exception("Can read" + file.getAbsolutePath());
                }
                result++;
            }
        } catch (Exception e) {
            System.out.println(e);
            return result - 1;
        }
    }

    private Monster getMonster(String code) throws InvalidMonsterCodeException {

        if (code == null || code.length() != 21) throw new InvalidMonsterCodeException(code);

        int body = Integer.parseInt(code.substring(0, 1), 16) % maxBody + 1;
        var monster = new Monster(body);
        int counter = 1;
        for (String part : parts) {
            monster.getParts().put(part, getPart(body, part, code.substring(counter, counter + 5)));
            counter += 5;
        }
        return monster;
    }

    private MonsterPart getPart(int body, String part, String code) {

        var id = Integer.parseInt(code.substring(0, 1), 16) % maxItem.get(part) + 1;
        var mp = new MonsterPart(body, part, id);
        mp.setX((int) (680 * getPercent(code.substring(1, 3))));
        mp.setY((int) (1000 * getPercent(code.substring(3, 5))));

        return mp;
    }

    private Double getPercent(String hex) {
        return Integer.parseInt(hex, 16) / 256.0;
    }

}
