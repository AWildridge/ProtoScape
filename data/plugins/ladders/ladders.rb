require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.def.StaticObjectDefinition'
java_import 'org.apollo.game.model.Position'

CLIMB_UP_ANIM = Animation.new(828, 0)

class ClimbLadderAction < DistancedAction

	attr_reader :@player, :@event
	
	def initialize(player, event)
		super 0, true, player, event.position, 1
		@player = player
		@event = event
	end
	
	def executeAction
		definition = StaticObjectDefinition.forId(@event.id)
		actionName = definition.actionNames[@event.option - 1].to_s
		playerPos = @player.position
		if (actionName.include? 'down') || (actionName.include? 'Down')
			if playerPos.height == 0
				climb(Position.new(playerPos.x, playerPos.y + 6400, playerPos.height))
			else
				climb(Position.new(playerPos.x, playerPos.y, playerPos.height - 1))
			end
		elsif (actionName.include? 'up') || (actionName.include? 'Up')
			if playerPos.y > 6400
				climb(Position.new(playerPos.x, playerPos.y - 6400, playerPos.height))
			else
				climb(Position.new(playerPos.x, playerPos.y, playerPos.height + 1))
			end
		end
	end
	
	def climb(position)
		@player.turnTo @event.position
		# It seems as if no animation is played when climbing a ladder from videos watched on youtube.
		@player.playAnimation CLIMB_UP_ANIM
		@player.teleport position
	end

end


on :event, :object_action do|ctx, player, event|
	definition = StaticObjectDefinition.forId(event.id)
	if definition.name.to_s.include? 'Ladder'
		player.startAction ClimbLadderAction.new(player, event)
		# ctx.breakChainHandler()
	end
end