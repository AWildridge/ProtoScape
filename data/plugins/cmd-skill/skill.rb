require 'java'
java_import 'org.apollo.game.model.SkillSet'

on :command, :max do |player, command|
  skills = player.skill_set
  (0...skills.size).each do |skill|
    skills.add_experience(skill, SkillSet::MAXIMUM_EXP)
  end
end

on :command, :reset do |player, command|
  skills = player.skill_set
  (0...skills.size).each do |skill|
    skills.set_skill(skill, Skill.new(1, 1, 1))
  end
end
