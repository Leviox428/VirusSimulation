import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Human extends Rectangle {
    private int sicknessDurationDays;
    private HumanStatus status;
    private double vectorX;
    private double vectorY;
    private int lifeDurationDays;
    private int immunityDurationDays;

    public Human(HumanStatus status) {
        super(10, 10);
        this.immunityDurationDays = 20;
        this.lifeDurationDays = 0;
        this.status = status;
    }   
    public Color getColor() {
        return this.status.getColor();
    }
    
    public HumanStatus getHumanStatus() {
        return this.status;
    }
    public void setHumanStatus(HumanStatus status) {
        this.status = status;
    }
    
    
    public double getVectorX() {
        return this.vectorX;
    }
    
    public double getVectorY() {
        return this.vectorY;
    }
    
    public void setVectorX(double vectorX) {
        this.vectorX = vectorX;
    }
    
    public void setVectorY(double vectorY) {
        this.vectorY = vectorY;
    }
    
    public int getLifeDuration() {
        return this.lifeDurationDays;
    }
    
    public void setLifeDuration(int time) {
        this.lifeDurationDays += time;
    }
    
    public void addSicknessDurationDays(int time) {
        this.sicknessDurationDays += time;
    }
    
    public int getSicknessDurationDays() {
        return this.sicknessDurationDays;
    }
    
    public void setImmunity(int time) {
        this.immunityDurationDays += time;
    }
    
    public int getImmunity() {
        return this.immunityDurationDays;
    }
    
}


