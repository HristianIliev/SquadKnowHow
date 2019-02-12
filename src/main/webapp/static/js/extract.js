$(window).on("load", function() {
  var painterSkillsEl = $("ul li");
  var painterSkills = [];
  for(var painterSkill = 0, i = 0; painterSkill < painterSkillsEl.length; painterSkill += 1, i++){
    painterSkills[i] = $(painterSkillsEl[painterSkill]).text();
  }

  function ProgrammingSkillsDb(programmingSkills){
    this.programmingSkills = programmingSkills;
  }

  var ProgrammingSkillsDb = new ProgrammingSkillsDb(painterSkills);

  $.ajax({
    url: "/api/populatePainterSkills",
    method: "POST",  
    data: JSON.stringify({
      skills: painterSkills
    }),
    contentType: "application/json",
    success: function(result) {
    }
  })
});