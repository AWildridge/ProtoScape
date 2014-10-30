require 'java'
java_import 'org.apollo.game.model.NPC'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Character'
java_import 'org.apollo.game.model.Character.InteractionMode'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Position'

class AttackAction < DistancedAction
  attr_reader :character, :retaliating
  
  def initialize(character, retaliating)
    super character.get_combat_cooldown_delay, !retaliating, character, character.get_interacting_character.get_position, character.get_active_combat_action.distance, character.get_interacting_character 
    @character = character
    @retaliating = retaliating
  end
  
  def execute_action
    character = get_character
    mode = character.get_interaction_mode
    target = character.get_interacting_character
    
    if character.is_destroyed
      iftarget != null && target.get_event_manager == null
      target.set_walk_overridden false
    end
    
    this.stop
    return
  end
    
  if target == null || target.is_destroyed || target.get_combat_state.is_dead || mode != InteractionMode.ATTACK
    character.reset_interacting_character
    
    if character.get_event_manager == null
      character.set_walk_overridden false
    end
    
    if target != null && target.get_event_manager == null
      target.set_walk_overridden false
    end
    
    target = null
    this.stop #the target disconnected / got removed
    return
  end

  action = character.get_active_combat_action

  if !action.can_hit character, target 
    character.get_combat_state.set_queued_spell null
    character.reset_interacting_character
    
    if character.get_event_manager == null
      character.set_walk_overridden false
    end
    
    if target.get_event_manager == null
      target.set_walk_overridden false
    end
    
    this.stop
    return
  end
  
  character.get_walking_queue.clear

  extra_distance = 0
    
  extra_distance = 3 if character.get_first_direction.to_integer != -1 || character.get_second_direction.to_integer != -1 && target.get_first_direction.to_integer != -1 || target.get_second_direction.to_integer != -1   

  #TODO: NPC Pathfinding. Then remove this.
  if !character.get_position.is_within_distance target.get_position, character.get_active_combat_action.distance(character.get_interacting_character) 
      this.stop
    end if character.get_event_manager == null
                    
  this.set_delay character.get_combat_cooldown_delay
  
  if target != null && mode == InteractionMode.ATTACK 
    action.hit character, target
    character.set_in_combat true
    target.set_in_combat true
  else 
    this.stop # will be restarted later if another attack starts
  end
end