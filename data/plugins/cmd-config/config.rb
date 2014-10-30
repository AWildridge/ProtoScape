require 'java'
java_import 'org.apollo.game.event.impl.SendConfigEvent'

on :command, :config do |player, command|
  args = command.arguments
  if (1..2).include? args.length
  id = args[0].to_i
  state = args.length == 2 ? args[1].to_i : 0

  player.send(SendConfigEvent.new(id, state));
  end
end