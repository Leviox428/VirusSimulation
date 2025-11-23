# 🧫 Virus Simulation

> A real-time, adjustable virus-spread simulation built with Java and JavaFX.

---

## 🖼️ Overview

Virus Simulation is an interactive desktop application that visualizes how a virus spreads, mutates, and fades within a small artificial population.
The user can dynamically adjust parameters such as infectivity, recovery chance, reproduction probability, human lifespan, simulation speed, and more — all while observing changes live on the simulation panel and in automatically updating charts.

The project demonstrates real-time animation, probabilistic modeling, collision detection, and responsive UI updates using JavaFX.

---

## 🧰 Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/-JavaFX-blue?logo=java&logoColor=white)

**Other Tools:**

![Git](https://img.shields.io/badge/-Git-F05032?logo=git&logoColor=white)

---

## 🎬 Showcase

### 🖼️ Screenshots
| Menu | Game |
|:--:|:--:|
| ![Screenshot 1](Showcase/menu.png) | ![Screenshot 2](Showcase/game.png) |


### 🎥 Gameplay

> ![Gameplay](Showcase/gameplay.gif)

---

## 💡 Project Highlights

- 🧍 Population with **healthy**, **infected**, **immune**, and **dead** states  
- 🦠 Adjustable parameters:
  - Infectivity
  - Recovery chance
  - Sickness duration
  - Reproduction chance
  - Human lifespan
  - Simulation tick speed
- 🏃 Random movement and collision-based infection spread  
- 📈 **Real-time line chart** displaying population changes  
- 🔄 Dynamic sliders synchronized with text fields  
- 💀 Life cycle simulation (aging, immunity loss, natural death)

---

## 🧭 How It Works

1. **Setup**
   - User configures all parameters.
   - Simulation initializes healthy and sick individuals at random positions.

2. **Simulation Loop (JavaFX Timeline)**
   - Movement & boundary wrapping  
   - Collision detection  
   - Infection, reproduction, recovery, and death calculations  
   - Immunity duration tracking  
   - Aging and lifespan checks  
   - Chart updates every few ticks  

3. **Visualization**
   - Individuals are colored squares:
     - 🟩 Healthy  
     - 🟥 Sick  
     - ⬜ Immune  
   - Line charts log totals over time.

---

## 📚 What I Learned

- Real-time simulation using **JavaFX Timeline**  
- Coordinating UI controls with live data  
- Implementing probabilistic modeling in Java  
- Efficient collision detection and movement systems  
- Dynamic JavaFX chart updates  
- Designing agent-based simulations

---

## 🏁 Conclusion

Virus Simulation demonstrates how simple rules and probabilities can lead to complex dynamics in population behavior.  
It showcases JavaFX’s ability to handle interactive simulations, animations, and real-time charts within a desktop application.

> 🧪 Adjust parameters, run experiments, and observe how small changes influence the spread of a virus!
