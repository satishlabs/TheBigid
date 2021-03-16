var app = angular
  .module("tbidApp");

app.constant('ID_TYPES', [
  {
    name: "Event",
    type: "EVENT",
    icon: "glyphicon-calendar"
  },
  {
    name: "Visual",
    type: "VISUAL",
    icon: "glyphicon-picture"
  },
  {
    name: "Question",
    type: "QUESTION",
    icon: "glyphicon-question-sign"
  },
  {
    name: "Link",
    type: "LINK",
    icon: "glyphicon-arrow-right"
  }
]);

app.constant('ALL_TYPES', [
  {
    name: "General",
    type: "GENERAL",
    icon: "glyphicon-info-sign"
  },
  {
    name: "Event",
    type: "EVENT",
    icon: "glyphicon-calendar"
  },
  {
    name: "Visual",
    type: "VISUAL",
    icon: "glyphicon-picture"
  },
  {
    name: "Question",
    type: "QUESTION",
    icon: "glyphicon-question-sign"
  },
  {
    name: "Link",
    type: "LINK",
    icon: "glyphicon-arrow-right"
  }
]);
app.constant('ID_CATEGORIES', [

  "BIG",
  "FUNNY",
  "CASUAL",
  "SERIOUS",
  "SMALL",
  "PROVOCATIVE",
  "____"

]);
app.constant('IMG_PATH',[
  "./temp/"
]);
