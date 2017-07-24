export default class LifecycleEvent {
  constructor(playerBot, assumedScreenTest, processingLifecycleStatus) {
    this.playerBot = playerBot;
    this.timeRequested = new Date();
    this.assumedScreenTest = assumedScreenTest;
    this.processingLifecycleStatus = processingLifecycleStatus;
  }

}
