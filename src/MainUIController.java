import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.util.Random;
import java.util.ArrayList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import java.util.List;
import javafx.scene.layout.Pane;
import java.io.IOException;

import javafx.scene.Node;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.util.Pair;
import java.util.Set;
import java.util.HashSet;

public class MainUIController {
    @FXML
    private LineChart<?, ?> populationChart;
    
    @FXML
    private Label tickCount;

    @FXML
    private Slider sliderTicks;
    @FXML
    private Slider sliderHumanCount;
    @FXML
    private Slider sliderInfectivity;
    @FXML
    private Slider sliderReproductionChance;
    @FXML
    private Slider sliderHumanLifeLength;
    @FXML
    private Slider sliderDuration;
    @FXML
    private Slider sliderChanceOfRecovery;

    @FXML
    private TextField fieldDays;
    @FXML
    private TextField fieldHumanCount;
    @FXML
    private TextField fieldInfectivity;
    @FXML
    private TextField fieldSicknessDurationDays;
    @FXML
    private TextField fieldReproductionChance;
    @FXML
    private TextField fieldHumanLifeLength;
    @FXML
    private TextField fieldRecoveryChance;

    @FXML
    private Pane paneSimulation;
    
    private XYChart.Series totalHumanCountXY;
    private XYChart.Series healthyHumansXY;
    private XYChart.Series sickHumansXY;
    private XYChart.Series immuneHumansXY;
    
    private Timeline timeline;
    private int humanLifeDurationLengthDays;
    private int reproductionChance;
    private int maximumHumanCount;
    private int ticksPerSecond;
    private double totalTickCount;
    private int infectivityChance;
    private int recoveryChance;
    private int sicknessDurationDays;
    private int collisionCheckCounter;

    @FXML
    public void initialize() {
        this.createChart();
        
        this.maximumHumanCount = 80;
        
        this.fieldReproductionChance.setText("20");
        this.fieldHumanLifeLength.setText("200");
        this.fieldHumanCount.setText("10");
        this.fieldInfectivity.setText("0");
        this.fieldDays.setText("0");
        this.fieldRecoveryChance.setText("0");
        this.fieldSicknessDurationDays.setText("1");
        this.totalTickCount = 0;
        this.ticksPerSecond = 1;
        this.timeline = new Timeline();
        this.sliderHumanCount.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.fieldHumanCount.setText(String.format("%.0f", newValue));
        });
        this.sliderInfectivity.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.fieldInfectivity.setText(String.format("%.0f", newValue));
        });
        this.sliderDuration.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.fieldSicknessDurationDays.setText(String.format("%.0f", newValue));
        });
        this.sliderChanceOfRecovery.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.fieldRecoveryChance.setText(String.format("%.0f", newValue));
        });
        this.sliderHumanLifeLength.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.fieldHumanLifeLength.setText(String.format("%.0f", newValue));
        });
        this.sliderReproductionChance.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.fieldReproductionChance.setText(String.format("%.0f", newValue));
        });
        
    }

    @FXML
    private void startSimulation(ActionEvent event) {
        this.timeline.play();
    }
    
    @FXML
    private void pauseSimulation(ActionEvent event) {
        this.timeline.stop();
    }

    @FXML
    private void setupSimulation(ActionEvent event) throws IOException {
        this.resetChart();
        this.paneSimulation.getChildren().clear();
        
        //Tick reset
        this.tickCount.setText("0");
        this.totalTickCount = 0;

        this.humanLifeDurationLengthDays = (int)this.sliderHumanLifeLength.getValue();
        this.reproductionChance = (int)this.sliderReproductionChance.getValue();
        this.infectivityChance = (int)this.sliderInfectivity.getValue();
        this.recoveryChance = (int)this.sliderChanceOfRecovery.getValue();
        this.sicknessDurationDays = (int)this.sliderDuration.getValue();

        this.fieldDays.setText("0");

        this.recoveryChance = (int)this.sliderChanceOfRecovery.getValue();
        this.infectivityChance = (int)this.sliderInfectivity.getValue();
        this.sicknessDurationDays = (int)this.sliderDuration.getValue();
        this.ticksPerSecond = (int)this.sliderTicks.getValue();

        //Simulation speed in ticks
        double tik = 1.0 / ticksPerSecond;

        //Stop old simulation loop
        this.timeline.stop();
        this.timeline.getKeyFrames().clear();
        
        //Create new simulation loop
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(tik), e -> this.simulationLoop());
        this.timeline.getKeyFrames().add(keyFrame);
        this.timeline.setCycleCount(Timeline.INDEFINITE);

        this.addHumanToSimulation(HumanStatus.SICK, 10);
        this.addHumanToSimulation(HumanStatus.HEALTHY, ((int)this.sliderHumanCount.getValue() - 10 ));
    }
    
    //Adding human to a random position in simulation
    private void addHumanToSimulation(HumanStatus status, int amount)  {
        Random random = new Random();

        for (int i = 0; i < amount; i++) {
            Human human = new Human(status);
            human.setFill(human.getColor());
            double maxX = this.paneSimulation.getWidth() - human.getWidth();
            double maxY = this.paneSimulation.getHeight() - human.getHeight();
            double randomX = random.nextDouble() * maxX;
            double randomY = random.nextDouble() * maxY;
            human.setVectorX(randomX);
            human.setVectorY(randomY);
            human.setLayoutX(randomX);
            human.setLayoutY(randomY);
            this.paneSimulation.getChildren().add(human);
        }        
    }
    
    private void removeHumanFromSimulation(Human human) {
        this.paneSimulation.getChildren().remove(human);
    }

    private void simulationLoop() {
        this.checkSickAndImmuneHumans();
        this.addTotalTickCountAndDays();
        this.checkHumansLifeCycle();
        this.moveHumans();

        int collisionCheckFrequency = 5;
        this.collisionCheckCounter++;
        if (this.collisionCheckCounter >= collisionCheckFrequency) {
            this.collisionsChecker();
            this.updateChart();
            this.collisionCheckCounter = 0;
        }
    }

    private void addTotalTickCountAndDays() {
        this.totalTickCount += 1;
        this.tickCount.setText(String.format("%.0f", this.totalTickCount));
        this.fieldDays.setText(String.format("%d", ((int)this.totalTickCount / 10)));
    }      

    private void moveHumans() {
        Random random = new Random();
        int randomMovement = random.nextInt(45);
        for (Node node : this.paneSimulation.getChildren()) {
            Human human = (Human)node;
            if (this.totalTickCount % 15 == randomMovement) {
                double randomX = random.nextDouble() * 2 - 1;
                double randomY = random.nextDouble() * 2 - 1;    
                human.setVectorX(randomX);
                human.setVectorY(randomY);
            } 
            
            if (node.localToParent(0, 0).getX() <= 0) {
                node.setLayoutX(this.paneSimulation.getWidth() - node.getBoundsInLocal().getWidth() - 10);
            }
            if (node.localToParent(0, 0).getX() + node.getBoundsInLocal().getWidth() >= this.paneSimulation.getWidth()) {
                node.setLayoutX(0);
            }
            if (node.localToParent(0, 0).getY() <= 0) {
                node.setLayoutY(this.paneSimulation.getHeight() - node.getBoundsInLocal().getHeight() - 10);
            }
            if (node.localToParent(0, 0).getY() + node.getBoundsInLocal().getHeight() >= this.paneSimulation.getHeight()) {
                node.setLayoutY(0);
            }
            double magnitude = Math.sqrt(human.getVectorX() * human.getVectorX() + human.getVectorY() * human.getVectorY());
            node.setLayoutX(node.localToParent(0, 0).getX() + 5 * human.getVectorX() / magnitude);
            node.setLayoutY(node.localToParent(0, 0).getY() + 5 * human.getVectorY() / magnitude);
        }
    }

    private boolean collisionCheck(Human human1, Human human2) {
        return human1.getBoundsInParent().intersects(human2.getBoundsInParent());
    }

    private void collisionsChecker() {
        Random random = new Random();
        Set<Pair<Human, Human>> collidedPairs = new HashSet<>();

        List<Node> humanNodes = this.paneSimulation.getChildren();
        int size = humanNodes.size();

        for (int i = 0; i < size; i++) {
            Human human1 = (Human)humanNodes.get(i);
            for (int j = i + 1; j < size; j++) {
                Human human2 = (Human)humanNodes.get(j);
                Pair<Human, Human> pair = new Pair<>(human1, human2);

                if (this.collisionCheck(human1, human2) && !collidedPairs.contains(pair)) {
                    this.collisionLogic(human1, human2, random, humanNodes);
                    collidedPairs.add(pair);
                }
            }
        }
    }

    private void collisionLogic(Human human1, Human human2, Random random, List<Node> zoznamLudi) {
        HumanStatus human1Status = human1.getHumanStatus();
        HumanStatus human2Status = human2.getHumanStatus();
        if (human1Status == HumanStatus.SICK && human2Status == HumanStatus.HEALTHY) {
            int infectionChance = random.nextInt(100);
            if (infectionChance < this.infectivityChance) {
                human2.setHumanStatus(HumanStatus.SICK);
                human2.setFill(human2.getColor());
                
            }
        } else if (human1Status == HumanStatus.HEALTHY && human2Status == HumanStatus.HEALTHY) {
            if (zoznamLudi.size() <= this.maximumHumanCount) {
                int randomNumber = random.nextInt(100);
                if (randomNumber < this.reproductionChance) {
                    this.addHumanToSimulation(HumanStatus.HEALTHY, 1);
                } 
            }
        }
    }
    
    private void checkSickAndImmuneHumans() {
        Random random = new Random();
        List<Human> humanList = new ArrayList<>();
        for (Node node : this.paneSimulation.getChildren()) {
            Human human = (Human)node;
            humanList.add(human);
        }
        for (Human human : humanList) {
            if (human.getHumanStatus() == HumanStatus.SICK) {
                human.addSicknessDurationDays(1);
                if (human.getSicknessDurationDays() == (this.sicknessDurationDays * 10)) {
                    int randomNumber = random.nextInt(100);
                    if (randomNumber < this.recoveryChance) {
                        human.setHumanStatus(HumanStatus.IMMUNY);
                        human.setFill(human.getColor());
                    } else {
                        this.removeHumanFromSimulation(human);
                    }


                }
            }
            if (human.getHumanStatus() == HumanStatus.IMMUNY) {
                human.setImmunity(1);
                if (human.getImmunity() == (20 * 10)) {
                    human.setHumanStatus(HumanStatus.HEALTHY);
                    human.setFill(human.getColor());
                }
            }
        }
    }
    
    private void checkHumansLifeCycle() {
        List<Human> humanList = new ArrayList<>();
        for (Node node : this.paneSimulation.getChildren()) {
            Human human = (Human)node;
            humanList.add(human);
        }

        for (Human human : humanList) {
            human.setLifeDuration(1);
            if (human.getLifeDuration() == (this.humanLifeDurationLengthDays * 10)) {
                this.removeHumanFromSimulation(human);
            }
        }
     }
    
    private void updateChart() {
        List<Human> humanList = new ArrayList<>();
        List<Human> healthyHumans = new ArrayList<>();
        List<Human> sickHumans = new ArrayList<>();
        List<Human> immuneHumans = new ArrayList<>();
        for (Node node : this.paneSimulation.getChildren()) {
            Human human = (Human)node;
            humanList.add(human);
            if (human.getHumanStatus() == HumanStatus.HEALTHY) {
                healthyHumans.add(human);
            }
            if (human.getHumanStatus() == HumanStatus.SICK) {
                sickHumans.add(human);
            } 
            if (human.getHumanStatus() == HumanStatus.IMMUNY) {
                immuneHumans.add(human);
            }
        }

        this.totalHumanCountXY.getData().add(new XYChart.Data<>((this.totalTickCount / 10), humanList.size()));
        this.healthyHumansXY.getData().add(new XYChart.Data<>((this.totalTickCount / 10), healthyHumans.size()));
        this.sickHumansXY.getData().add(new XYChart.Data<>((this.totalTickCount / 10), sickHumans.size()));
        this.immuneHumansXY.getData().add(new XYChart.Data<>((this.totalTickCount / 10), immuneHumans.size()));
    }

    private void createChart() {
        this.totalHumanCountXY = new XYChart.Series();
        this.totalHumanCountXY.setName("Together");
        this.healthyHumansXY = new XYChart.Series();
        this.healthyHumansXY.setName("Healthy");
        this.sickHumansXY = new XYChart.Series();
        this.sickHumansXY.setName("Infected");
        this.immuneHumansXY = new XYChart.Series();
        this.immuneHumansXY.setName("Immune");
        this.populationChart.getData().addAll(this.totalHumanCountXY, this.healthyHumansXY, this.immuneHumansXY, this.sickHumansXY);
    }
    
    // Reset values for all series in the line chart
    public void resetChart() {
        ArrayList<XYChart.Series> allSeries = new ArrayList<>();
        allSeries.addAll(this.populationChart.getData());

        for (XYChart.Series<Number, Number> series : allSeries) {
            for (XYChart.Data<Number, Number> data : series.getData()) {
                data.setXValue(0); 
                data.setYValue(0); 
            }
        }
    }

}



