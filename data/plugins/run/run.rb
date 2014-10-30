require 'java'

on :button, 153 do |player|
  player.setRunning(true)
end
on :button, 152 do |player|
  player.setRunning(false)
end