IoT Scavenger Hunt
=========


This is a demo application that uses the [EVRYTHNG](https://github.com/evrythng) API.

[Here there is an online demo.](http://safe-coast-6679.herokuapp.com/)

This is a very simple Play Framework application that simulates the Scavenger Hunt game using the Internet of Things devices or tags. 

It assumes that a certain number of devices or tags have been spreaded around the city and that each one has its own virtual online rapresentation in the EVRYTHNG backend. 

We can use them as check-in points of the hunt. Each one of them will have a specific location and will contain the clue information about the next location in the hunt. 

Once you solved the clue and found the next place/tag, it will give you the next hint. Till you arrive to the final place. 

The application has an Admin Panel from which it is possible to see the current Hunt steps on a map. And then it has a Game section that simulate the game behaviour.  




Installation
--------------
To run this application you will need Play Framework 2.2 installed. You can then run it as follow:

```sh
git clone https://github.com/pcalessio/evrythngDemo.git
play run
```
Note: you need to change the evrythng.apikey configuration in application.conf in order to run it. 
