package swinbank.server.policy;

/**
 *
 * @author James
 */
public enum AccountType {
    Biller('B'),
    Standard('S');

    private Character _charRepresentation;

    AccountType(Character c)
    {
        _charRepresentation = c;
    }

    public Character getCharRepresentation()
    {
        return _charRepresentation;
    }
}


