# What is mtgo-best-bot?
A GPL-based bot network tool that strictly adheres to Wizards of the Coast, a Hasbro subsidiary's Magic: the Gathering Online Terms of Service. If at any point ToS is broken, this repository must be immediately notified in writing of the specific violation.

It is a modularized Maven-based project utilizing Spring 4. Each module has a very specific purpose. I will put specific README(s) in more complicated projects. Essentially there is a 'bot client' that needs an entire Windows VM with MTGO running to itself. Ideally it will hook into a Kafka instance which manages publishing and subscribing to topics pertinent to intra-bot communication. Theoretically this system can easily manage a store across an arbitrary number of bots.

# Contributing?
I can deal with arbitrary pull quests. The end game is to dockerize/containerize this bad boy so true Magic: the Gathering bots can be yielded to the masses.

# Conclusion?
Godspeed.
