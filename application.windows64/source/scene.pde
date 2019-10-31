class scene {
  //attributes
  String panelName;
  String infoText, originalInfoText;
  String optionText1, optionText2;
  Button select1, select2;
  PImage background;
  PImage foreground;
  Button[] buttonsInScene;
  boolean setup;
  boolean sceneChange;
  boolean gameOver;
  Stopwatch watch;

  //constructor
  scene(String p1, String p2, String p3, String p4, String p5, String p6) {
    select1 = new Button(width/2, 655, 1280, 30);
    select2 = new Button(width/2, 685, 1280, 30);
    panelName = p1;
    infoText = p2;
    originalInfoText = p2;
    optionText1 = p3;
    optionText2 = p4;
    background = loadImage(p5);
    foreground = loadImage(p6);
    if (panelName == "Island") { //this changes the size of the buttonsInScene array and the buttons within it depending on which scene it is. This allows for different buttons in each scene
      println("creating island buttons");
      buttonsInScene = new Button[3];
      buttonsInScene[0] = new Button(250, 300, 650, 200);
      buttonsInScene[1] = new Button(900, 400, 512, 476);
      buttonsInScene[2] = new Button(200, 500, 64, 64);
    } else if (panelName == "Island-B") {
      println("creating islandB buttons");
      buttonsInScene = new Button[2];
      buttonsInScene[0] = new Button(width/2, 500, 128, 128);
      buttonsInScene[1] = new Button(1150, 150, 128, 64);
    } else if (panelName == "IslandB-B") {
      println("craetingislandB-B buttons");
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(435, 500, 64, 64);
    } else if (panelName == "cave") {
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(400, 300, 200, 350);
    } else if (panelName == "caveB") {
      buttonsInScene = new Button[2];
      buttonsInScene[0] = new Button(width/4, height/2, width/2, 350);
      buttonsInScene[1] = new Button(width/4*3, height/2, width/2, 350);
    } else if (panelName == "caveB-A") {
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(width/2, 400, 64, 64);
    } else if (panelName == "caveA") {
      buttonsInScene = new Button[1];
      buttonsInScene[0] = new Button(width/2, height/2, 64, 64);
    } else {
      buttonsInScene = new Button[0];
    }
  }

  //methods

  void drawBackground() {
    background(255);
    image(background, width/2, height/2); //the base of every scene, draws objects and backgrounds
    drawObjects();
    fill(0);

    rectMode(CORNERS);
    rect(0, 0, width, height/8);
    rect(0, (height/8)*7, width, height); //draws black bars

    //next lines are all used for the text that is going to be displayed at the top and bottom
    fill(255);
    textAlign(CENTER);
    textSize(40);
    textSize(20);
    text(panelName, width/2, 25);
    textSize(22);
    text(infoText, 0, 30, 1280, 200);
    if (select1.hover == false) {
      textSize(22);
      text(optionText1, width/2, 660);
    } else if (select1.hover == true) { //makes the text change size when it is hovered over
      textSize(25);  
      text(optionText1, width/2, 660);
    }
    if (select2.hover == false) {
      textSize(22);
      text(optionText2, width/2, 690);
    } else if (select2.hover == true) {
      textSize(25);
      text(optionText2, width/2, 690);
    }
    image(foreground, width/2, height/2);

    prev.checkHover();
    fill(0, 50);  
    rectMode(CENTER);
    rect(prev.pos.x, prev.pos.y-10, 100, 30);  
    if (!prev.hover) {
      textAlign(CENTER);
      fill(255);
      textSize(22);
      text("Go Back", prev.pos.x, prev.pos.y);
    } else if (prev.hover) {
      fill(255);
      textAlign(CENTER);
      textSize(25);
      text("Go back", prev.pos.x, prev.pos.y);
    }
    checkGameOver(); //checks if the player has died

    if (mousePressed) { //special case for if the player has the map. Clicking whilst the map is equipped will always update the info text
      if (items[3].equipped) {
        infoText = "The map seems to lead to the deepest part of the forest";
      }
    }
  }

  void checkGameOver() { //draws an image over the top of everything when the player dies and allows them to return to the previous scene
    if (gameOver) {
      fill(0);
      rectMode(CORNERS);
      rect(0, 0, width, height/8);
      rect(0, (height/8)*7, width, height); 
      PImage endScreen = loadImage("gameOver.png");
      image(endScreen, width/2, height/2);
      infoText = "You died. Click anywhere to return to previous scene.";
      fill(255);
      textSize(22);
      text(infoText, 0, 30, 1280, 200);
      if (mousePressed) {
        placeInStory = placeInStory.prev; 
        placeInStory.value.setup = true;
        gameOver = false;
      }
    }
  }


  //draw objects is called in a different way for each scene
  //this is where all anuimation is listed
  //infotext can also be updated here
  //each scene has a setup attribute that will be called once every time the player enters that scene
  //there are some special cases in this that allow the program to function slightly differently depending on which scene the user is on


  void drawObjects() {
    if (panelName == "Intro") {
      if (setup) {
        infoText = originalInfoText;
        chest.resetObject();
        chest.moveBack = false;
        chest.scaleBack = false;
        setup = false;
      }
      chest.drawObject();
      imageMode(CENTER);
      chest.moveAlongVector(width/2, height/2, false);
      chest.scaleAlongVector(512+64, 384+64, false);
    }

    if (panelName == "Intro-A-A") {
      if (setup) {
        infoText = originalInfoText;
        shore.resetObject();
        shore.moveBack = false;
        rock.resetObject();
        rock.moveBack = false;
        rock2.resetObject();
        rock2.moveBack = false;
        setup = false;
      }
      shore.drawObject();
      rock.drawObject();
      rock2.drawObject();
      imageMode(CENTER);
      shore.moveAlongVector(width/2, 360, false);
      rock.moveAlongVector(100, height, false);
      rock2.moveAlongVector(1100, height, false);
    }

    if (panelName == "Intro-A") {
      if (setup) {
        infoText = originalInfoText;
        guard1.resetObject();
        guard1.moveBack = false;
        guard2.resetObject();
        guard2.moveBack = false;
        setup = false;
      }
      chest.drawObject();
      guard1.drawObject();
      guard2.drawObject();
      imageMode(CENTER);
      guard1.moveAlongVector(300, 400, false);
      guard2.moveAlongVector(980, 400, false);
      guard1.moveAlongVector(300, 400, false);
      guard2.moveAlongVector(980, 400, false);
    }

    if (panelName == "Island") {
      if (setup) {
        infoText = originalInfoText; 
        buttonsInScene[0].clicked = false;
        buttonsInScene[1].clicked = false;
        bird.resetObject();
        bird.scaleBack = false;
        bird2.resetObject();
        bird2.scaleBack = false;
        bird3.resetObject();
        bird3.scaleBack = false;
        setup = false;
      }

      tree2.drawObject();
      tree3.drawObject();
      water.drawObject();
      tree.drawObject();
      String[] birdAnimate = {"bird1.png", "bird2.png", "bird3.png", "bird4.png", "bird5.png"}; // the first example of setting up an animation
      bird.animate(birdAnimate, true);
      bird.drawObject();
      bird.scaleAlongVector(0, 0, false);
      bird2.animate(birdAnimate, true);
      bird2.drawObject();
      bird2.scaleAlongVector(0, 0, false);
      bird3.animate(birdAnimate, true);
      bird3.drawObject();
      bird3.scaleAlongVector(0, 0, false);
      String[] chickenAnimate = {"chicken1.png", "chicken2.png", "chicken3.png", "chicken4.png", "chicken3.png", "chicken2.png", "chicken1.png", "chicken1.png", "chicken1.png", "chicken1.png"};
      chicken.animate(chickenAnimate, true);
      chicken.drawObject();

      water.moveAlongVector(width/2, 500, true);
      if (buttonsInScene[0].clicked) {
        infoText = "It's a cave. It could be dangerous, but exploring it will likely become necessary";
        buttonsInScene[0].clicked = false;
      }
      if (buttonsInScene[1].clicked) {
        infoText = "An ordinary palm tree, this place is littered with them"; 
        buttonsInScene[1].clicked = false;
      }
      if (buttonsInScene[2].clicked) {
        infoText = "What would you know, a chicken"; 
        if (items[0].equipped == true) {
          infoText = "You won't gain anything by killing this innocent chicken, you monster";
        }
        buttonsInScene[2].clicked = false;
      }
    }

    if (panelName == "Island-B") {
      if (setup) {
        sceneChange = false;
        infoText = originalInfoText; 
        shrubA.moveBack = false;
        shrubB.moveBack = false;
        shrubA.resetObject();
        shrubB.resetObject();
        setup = false;
      }
      if (!items[2].aquired) {
        String[] shovelAnimate = {"shovel1.png", "shovel2.png", "shovel3.png"};
        shovel.animate(shovelAnimate, true);
        shovel.drawObject();
      }
      String[] monkeyStandAnimate = {"monkeyStand1.png", "monkeyStand2.png", "monkeyStand3.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png", "monkeyStand5.png", "monkeyStand4.png"}; 
      monkeyStand.drawObject();
      shrubs.drawObject();
      shrubA.drawObject();
      shrubA.moveAlongVector(100, height/2, false);
      shrubA.moveAlongVector(100, height/2, false);
      shrubB.drawObject();
      shrubB.moveAlongVector(1180, height/2, false);
      shrubB.moveAlongVector(1180, height/2, false);

      if (buttonsInScene[0].clicked) {
        infoText = "It's an oasis. You see something shining in the water and grab it. You have obtained a shovel, it could be useful for digging treasure";
        items[2].aquired = true;
        buttonsInScene[0].clicked = false;
      }
      if (buttonsInScene[1].clicked) {
        infoText = "The monkey ran away! It looks like it ran to the North Shore";
        sceneChange = true;
        buttonsInScene[1].clicked = false;
      }
      if (sceneChange) {
        monkeyStand.animate(monkeyStandAnimate, true);
        monkeyStand.moveAlongVector(1500, 0, false);
      }
    }

    if (panelName == "IslandB-B") {
      if (setup) {
        setup = false;
        buttonsInScene[0].clicked = false;
      }
      water2.drawObject();
      water2.moveAlongVector(width/2, height/2-20, true);
      String[] monkeyAnimate = {"monkey1.png", "monkey2.png", "monkey3.png", "monkey4.png", "monkey3.png", "monkey2.png", "monkey1.png", "monkey5.png", "monkey6.png", "monkey7.png", "monkey6.png", "monkey5.png", "monkey1.png", };
      monkey.animate(monkeyAnimate, true);
      if (story[5].sceneChange) {
        monkey.drawObject();
        if (!items[0].aquired) {
          infoText = originalInfoText + " It would appear the monkey that ran away dropped that gun here";
          gunProp.drawObject();
        }
        if (buttonsInScene[0].clicked) {
          items[0].aquired = true;
          infoText = "You aquired a flintlock pistol. This will come in useful for dealing with danger";
          buttonsInScene[0].clicked = false;
        }
      }
    }
    if (panelName == "IslandB-A") {
      if (items[3].aquired && sceneChange == false) {
        infoText = "This seems to be where the map leads to. Perhaps you should use your shovel and try digging"; 
        sceneChange = true;
      }
      if (items[3].aquired && items[2].equipped) {
        //This code sets up the irreversable part of the story (The Fanale) and changes the tree routes or order to only allow the player to progress once they have finished the main quest  
        if (mousePressed) {
          placeInStory.left = new BTNode(story[12], null);
          placeInStory.right = null;
          placeInStory.left.left = new BTNode(story[13], null);
          placeInStory.left.left.left = new BTNode(story[14], null);
          //These two operate as the final decisions in the story
          placeInStory.left.left.left.left = new BTNode(story[15], null);
          placeInStory.left.left.left.right = new BTNode(story[16], null);
          //Story is moved forward here
          placeInStory = placeInStory.left;
        }
      }
    }

    if (panelName == "cave") {
      if (setup) {
        setup = false;
      }
      if (buttonsInScene[0].clicked && !items[1].equipped) {
        infoText = "You'll need to use a key to open this door";
        buttonsInScene[0].clicked = false;
      } else if (buttonsInScene[0].clicked && items[1].equipped) {
        sceneChange = true;
      } else if (!sceneChange) {
        door.drawObject();
      }
      if (sceneChange) {
        infoText = "You used the key to open the door, a new path is available"; 
        optionText1 = "Enter door";
        doorOpened.drawObject();
      }
    }
    if (panelName == "caveB") {
      if (setup) {
        watch = new Stopwatch(600); 
        watch.resetClock();
        skeleton1.moveBack = false;
        skeleton2.moveBack = false;
        skeleton1.scaleBack = false;
        skeleton2.scaleBack = false;
        skeleton1.resetObject();
        skeleton2.resetObject();
        setup = false;
      }
      watch.startClock();
      if (watch.active && !sceneChange) {  
        String time = str(watch.time/60);
        println(time);
        textSize(22);
        fill(255, 0, 0);
        text("Time remaining: " + time, width/2, 200);
        skeleton1.moveAlongVector(width/2-100, 600, false);
        skeleton2.moveAlongVector(width/2+100, 600, false);
        skeleton1.scaleAlongVector(256*2, 288*2, false);
        skeleton2.scaleAlongVector(256*2, 288*2, false);
        if (buttonsInScene[0].clicked && items[0].equipped) {
          if (buttonsInScene[1].clicked) {
            sceneChange = true;
          }
        } else if (!buttonsInScene[0].clicked || !items[0].equipped) {
          skeleton1.drawObject();
          buttonsInScene[0].clicked = false;
        }
        if (buttonsInScene[1].clicked && items[0].equipped) { 
          if (buttonsInScene[0].clicked) {
            sceneChange = true;
          }
        } else if (!buttonsInScene[1].clicked || !items[0].equipped) {
          skeleton2.drawObject();
          buttonsInScene[1].clicked = false;
        }
      } else if (!watch.active && !sceneChange) {
        println("lose");
        gameOver = true;
      } else if (sceneChange) {
        infoText = "You defeated the skeletons! You can now venture futher in to the cavern.";
        optionText1 = "Go deeper in to the caverns";
      }
    }

    if (panelName == "caveB-A") {
      if (setup) {
        setup = false;
      }
      if (buttonsInScene[0].clicked) {
        items[1].aquired = true;
        infoText = "You found a key in the dark room. What could it open?";
      } else {
        keyProp.drawObject();
      }
    }

    if (panelName == "caveA") {
      if (setup) {  
        setup = false;
      }
      if (buttonsInScene[0].clicked) {
        items[3].aquired = true;
        infoText = "You found the map. It seems to lead to the deepest point of the forest. If you have a shovel, go there and use it on the ground";
      } else {
        mapProp.drawObject();
      }
    }

    if (panelName == "Finale") {
      if (setup) {
        skull.moveBack = false;
        skull.resetObject();
        setup = false;
      }
      skull.drawObject();
      skull.moveAlongVector(width/2, height/2, false);
    }

    if (panelName == "Finale-A") {
      if (setup) {
        ship.moveBack = false;
        ship.resetObject();
        setup = false;
      }
      ship.drawObject();
      ship.moveAlongVector(width/2, height/2, false);
    }
    if (panelName == "Finale-A-A") {
      if (setup) {
        guard3.moveBack = false;
        guard3.resetObject();
        setup = false;
      }
      ship.drawObject();
      guard3.drawObject();
      guard3.moveAlongVector(300, height/2, false);
    }
  }
}
