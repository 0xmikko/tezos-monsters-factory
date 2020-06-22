package courses.monsters.tezos.exceptions;

public class InvalidMonsterCodeException extends RuntimeException{
    public InvalidMonsterCodeException(String code) {
        super("Invalid code" + code);
    }
}
