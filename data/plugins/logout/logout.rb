LOGOUT_BUTTON_ID = 2458

on :button, LOGOUT_BUTTON_ID do |player|
  if(player.getCombatState().getLastDamageEventTimer() > (Time.now.to_i * 1000))
    player.send_message "You can't logout until 10 seconds after the end of combat."
	  player.send_message "Please wait #{player.getCombatState().getLastDamageEventTimer() - Time.now.to_i} seconds to logout."
    return
  end
  player.logout
end
