package model;

public class Gerente extends User {

    // Number of staff under supervision
    private int personalACargoCount;

    public int getPersonalACargoCount() {
        return personalACargoCount;
    }

    public void setPersonalACargoCount(int personalACargoCount) {
        this.personalACargoCount = personalACargoCount;
    }

}
