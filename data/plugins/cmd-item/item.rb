require 'java'
java_import 'org.apollo.game.event.impl.OpenInterfaceEvent'

on :command, :item do |player, command|
  args = command.arguments
  if (1..2).include? args.length
    id = args[0].to_i
    amount = args.length == 2 ? args[1].to_i : 1

    player.inventory.add id, amount
  else
    player.send_message "Syntax: ::item [id] [amount=1]"
  end
end

on :command, :destroy do |player, command|
  args = command.arguments
  if (1..2).include? args.length
    id = args[0].to_i
    amount = args.length == 2 ? args[1].to_i : 1

    player.inventory.remove id, amount
  else
    player.send_message "Syntax: ::destroy [id] [amount=1]"
  end
end

on :command, :empty do |player, command|
  player.inventory.clear
end

on :command, :interface do |player, command|
  args = command.arguments
  id = args[0].to_i
  player.send(OpenInterfaceEvent.new(id))
end
