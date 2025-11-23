import javafx.scene.paint.Color;

public enum HumanStatus {
    SICK(Color.RED),
    HEALTHY(Color.GREEN),
    IMMUNY(Color.GREY);
    
    private final Color color;
    HumanStatus(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return this.color;
    }
}
