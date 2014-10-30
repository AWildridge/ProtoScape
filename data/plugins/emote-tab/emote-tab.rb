require 'java'
java_import 'org.apollo.game.model.Animation'

on :button, 168 do |player|
  player.play_animation Animation::YES
end
on :button, 169 do |player|
  player.play_animation Animation::NO
end
on :button, 162 do |player|
  player.play_animation Animation::THINKING
end
on :button, 164 do |player|
  player.play_animation Animation::BOW
end
on :button, 165 do |player|
  player.play_animation Animation::ANGRY
end
on :button, 161 do |player|
  player.play_animation Animation::CRY
end
on :button, 170 do |player|
  player.play_animation Animation::LAUGH
end
on :button, 171 do |player|
  player.play_animation Animation::CHEER
end
on :button, 163 do |player|
  player.play_animation Animation::WAVE
end
on :button, 167 do |player|
  player.play_animation Animation::BECKON
end
on :button, 172 do |player|
  player.play_animation Animation::CLAP
end
on :button, 166 do |player|
  player.play_animation Animation::DANCE
end
on :button, 13362 do |player|
  player.play_animation Animation::PANIC
end
on :button, 13363 do |player|
  player.play_animation Animation::JIG
end
on :button, 13364 do |player|
  player.play_animation Animation::SPIN
end
on :button, 13365 do |player|
  player.play_animation Animation::HEAD_BANG
end
on :button, 13366 do |player|
  player.play_animation Animation::JOY_JUMP
end
on :button, 13367 do |player|
  player.play_animation Animation::RASPBERRY
end
on :button, 13368 do |player|
  player.play_animation Animation::YAWN
end
on :button, 13369 do |player|
  player.play_animation Animation::SALUTE
end
on :button, 13370 do |player|
  player.play_animation Animation::SHRUG
end
on :button, 11100 do |player|
  player.play_animation Animation::BLOW_KISS
end
on :button, 667 do |player|
  player.play_animation Animation::GLASS_BOX
end
on :button, 6503 do |player|
  player.play_animation Animation::CLIMB_ROPE
end
on :button, 6506 do |player|
  player.play_animation Animation::LEAN
end
on :button, 666 do |player|
  player.play_animation Animation::GLASS_WALL
end
on :button, 13383 do |player|
  player.play_animation Animation::GOBLIN_BOW
end
on :button, 13384 do |player|
  player.play_animation Animation::GOBLIN_DANCE
end