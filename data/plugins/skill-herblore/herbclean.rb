require 'java'

java_import 'org.apollo.game.model.Skill'

def find_slot(id)
  dirty_ids = [199, 201, 203, 205, 207, 3049, 209, 211, 213, 3051, 215, 2485, 217, 219]
  for i in 0..dirty_ids.length
    if(dirty_ids[i] == id)
	  return i;
	end
  end
  return nil
end

def cleanherb(player, item, index)
  clean_ids = [249, 251, 253, 255, 257, 2998, 259, 261, 263, 3000, 265, 2481, 267, 269]
  level_reqs = [3, 5, 11, 20, 25, 30, 40, 48, 54, 59, 65, 67, 70, 75]
  exps = [2.5, 3.8, 5.0, 6.3, 7.5, 8.0, 8.8, 10.0, 11.3, 11.8, 12.5, 13.1, 13.8, 15.0]
  skills = player.skill_set
  herblore_level = skills.get_skill(Skill::HERBLORE).maximum_level
  
  if(herblore_level < level_reqs[index])
    player.send_message "You need a herblore level of #{level_req[index]} to clean this herb."
	return
  end
  player.inventory.remove(item)
  player.inventory.add clean_ids[index]
  player.send_message "You managed to identify this herb."
  skills.add_experience Skill::HERBLORE, exps[index]
end

on :event, :ConsumeItemAction do |ctx, player, event|
  itemId = event.getId()
  if find_slot(itemId).nil?
    return;
  else
	cleanherb(player, itemId, find_slot(event.getId()))
  end
end