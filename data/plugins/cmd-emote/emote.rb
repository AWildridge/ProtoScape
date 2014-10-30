require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.NPCManager'

on :command, :emote do |player, command|
  args = command.arguments
  if args.length == 1
    id = args[0].to_i
    player.play_animation Animation.new(id)
  end
end

on :command, :pnpc do |player, command|
  args = command.arguments
  if args.length == 1
    id = args[0].to_i
    player.update_to_npc id
  end
end

on :command, :npc do |player, command|
	args = command.arguments
	if args.length == 1
		id = args[0].to_i
		NPCManager.appendNpc(id, player.getPosition())
	end
end

on :command, :indexedemote do |player, command|
  args = command.arguments
  if args.length == 1
    start = args[0].to_i
    player.start_action CycleAnimationsAction.new(player, start)
  end
end

class CycleAnimationsAction < Action
  attr_reader :player, :id
     
  def initialize(player, beginning_id)
    super 5, true, player
    @player = player
    @id = beginning_id
  end
     
  def execute
    player.play_animation Animation.new(id)
    player.send_message "This is emote: " + id.to_s()
    @id += 1
  end
     
end
