package org.apollo.game.event.handler.impl;

import org.apollo.game.action.impl.DoorAction;
import org.apollo.game.content.ObjectMisc;
import org.apollo.game.content.skills.runecrafting.RuneCrafting;
import org.apollo.game.content.skills.smithing.Smithing;
import org.apollo.game.content.skills.thieving.Thieving;
import org.apollo.game.content.skills.woodcutting.TreeData;
import org.apollo.game.content.skills.woodcutting.Woodcutting;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ObjectActionEvent;
import org.apollo.game.event.impl.PositionEvent;
import org.apollo.game.event.impl.CreateObjectEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.inter.dialog.DialogueSender;
import org.apollo.game.model.inter.dialog.impl.LadderListener;
import org.apollo.game.model.obj.StaticObject;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * ObjectEventHandler.java
 * @author The Wanderer
 */
public class ObjectEventHandler extends EventHandler<ObjectActionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, final Player player, final ObjectActionEvent event) {
        if (player.getPosition().isWithinInteractionDistance(event.getPosition())) {
            player.turnTo(event.getPosition());
            StaticObject object = World.getWorld().getRegionRepository().getStaticObject(event.getPosition(), event.getId());
            if (event.getOption() == 1) {
                if (DoorAction.isDoor(event.getId(), 0)) {
                    player.startAction(new DoorAction(object, player));
                }
                TreeData tree = TreeData.get(event.getId());
                if (tree != null) {
                    tree.setObjectIdUsing(event.getId());
                    Woodcutting.registerChopAction(player, tree, event.getPosition());
                    return;
                }
                switch (event.getId()) {
                    /* case 1530:
                    //start of doors..
                    Position pos = new Position(player.getPosition().getX(), player.getPosition().getY() + 1);
                    player.sendNewObject(pos, 22, 1, 10);
                    break;*/
                    case 12965:
                        DialogueSender.sendOptionTwoLines(player, new LadderListener(player), "Up", "Down", 5);
                        break;
                    case 9357:
                        player.getFightCaves().assignReward(false);
                        break;
                    case 9356:
                        player.getFightCaves().enterCaves();
                        break;
                    case 8150:
                        player.getFarming().herbHarvesting(event.getId());
                        break;
                    case 2478:
                    case 2479:
                    case 2480:
                    case 2481:
                    case 2482:
                    case 2483:
                    case 2484:
                    case 2485:
                    case 2486:
                    case 2487:
                    case 2488:
                        RuneCrafting.handleRuneCrafting(player, event.getId());
                        break;

                    case 7129:
                    case 7130:
                    case 7131:
                    case 7132:
                    case 7133:
                    case 7134:
                    case 7135:
                    case 7136:
                    case 7137:
                    case 7138:
                    case 7139:
                    case 7140:
                        RuneCrafting.handleRifts(player, event.getId());
                        break;

                    case 2473:
                        player.teleport(new Position(2867, 3020));
                        break;

                    case 2465:
                        player.teleport(new Position(2984, 3291));
                        break;

                    case 2467:
                        player.teleport(new Position(3185, 3163));
                        break;

                    case 2468:
                        player.teleport(new Position(3306, 3476));
                        break;

                    case 2469:
                        player.teleport(new Position(3312, 3253));
                        break;

                    case 2466:
                        player.teleport(new Position(2980, 3514));
                        break;

                    case 2470:
                        player.teleport(new Position(3053, 3443));
                        break;

                    case 2474:
                        player.teleport(new Position(3061, 3593));
                        break;

                    case 2475:
                        player.teleport(new Position(1862, 4639));
                        break;

                    case 2471:
                        player.teleport(new Position(2407, 4379));
                        break;

                    case 2472:
                        player.teleport(new Position(2858, 3379));
                        break;

                    case 5280:
                        player.teleport(new Position(3666, 3522, 1));
                        break;

                    case 5281:
                        player.teleport(new Position(3666, 3517, 0));
                        break;

                    case 5267:
                        Position position = new Position(3653, 3520);
                        player.send(new PositionEvent(position, position));
                        player.send(new CreateObjectEvent(5282, 0, 10));
                        break;

                    case 9300:
                        if (player.hasAttributeTag("jumping") || (player.getPosition().getX() != 3240 && player.getPosition().getY() != 3334)) {
                            return;
                        }
                        Position step = new Position(player.getPosition().getX(), player.getPosition().getY() - 2);
                        player.getWalkingQueue().addStep(step);
                        player.getAttributeTags().add("jumping");
                        World.getWorld().schedule(new ScheduledTask(4, false) {

                            @Override
                            public void execute() {
                                Position fence = new Position(player.getPosition().getX(), player.getPosition().getY() + 3);
                                player.getWalkingQueue().addStep(fence);
                                World.getWorld().schedule(new ScheduledTask(1, false) {

                                    @Override
                                    public void execute() {
                                        player.playAnimation(Animation.create(2750));
                                        player.getAttributeTags().remove("jumping");
                                        stop();
                                    }
                                });
                                stop();
                            }
                        });
                        break;
                }
            } else if (event.getOption() == 2) {
                if (DoorAction.isDoor(object.getDefinition().getId(), 1)) {
                    player.startAction(new DoorAction(object, player));
                    return;
                }
                switch (event.getId()) {
                    case 11666:
                        Smithing.setupSmeltingInterface(player, event.getId(), event.getPosition());
                        break;
                    //TODO: handle these object picking things through another system.
                    case 312:
                        //Find a way to delete the stupid object >.>
                        //player.sendNewObject(event.getPosition(), -1, 1, 10);
                        ObjectMisc.pickObject(player, event, 1942);
                        break;
                    case 5585:
                        //Find a way to delete the stupid object >.>
                        //player.sendNewObject(event.getPosition(), -1, 1, 10);
                        ObjectMisc.pickObject(player, event, 1947);
                        break;
                    case 4706:
                    case 4708:
                    case 2561:
                    case 630:
                    case 6163:
                    case 4876:
                    case 4874:
                    case 6166:
                    case 4875:
                    case 635:
                    case 6574:
                    case 2793:
                    case 629:
                    case 2560:
                    case 6568:
                    case 14011:
                    case 7053:
                    case 632:
                    case 2563:
                    case 4278:
                    case 6571:
                    case 4705:
                    case 4707:
                    case 4277:
                    case 628:
                    case 2565:
                    case 6164:
                    case 4877:
                    case 633:
                    case 2564:
                    case 6572:
                    case 4878:
                    case 631:
                    case 2562:
                    case 6162:
                    case 6570:
                        Thieving.thieveStalls(player, event.getId(), event.getPosition());
                        break;
                }
            }
        }
    }
}
