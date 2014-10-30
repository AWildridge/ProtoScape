package org.apollo.net.release.r317;

import org.apollo.game.event.impl.BuildFriendsEvent;
import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.DisplayTabInterfaceEvent;
import org.apollo.game.event.impl.EnterAmountEvent;
import org.apollo.game.event.impl.IdAssignmentEvent;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.event.impl.PlayerMenuEvent;
import org.apollo.game.event.impl.ChatHeadAnimationEvent;
import org.apollo.game.event.impl.NPCSynchronizationEvent;
import org.apollo.game.event.impl.ObjectAnimationEvent;
import org.apollo.game.event.impl.OpenChatInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceDialogueEvent;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceSidebarEvent;
import org.apollo.game.event.impl.PlaySongEvent;
import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.event.impl.PositionEvent;
import org.apollo.game.event.impl.PrivateChatEvent;
import org.apollo.game.event.impl.PrivateChatLoadedEvent;
import org.apollo.game.event.impl.RegionChangeEvent;
import org.apollo.game.event.impl.RemoveGroundItemEvent;
import org.apollo.game.event.impl.RemoveObjectEvent;
import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.event.impl.SendGroundItemEvent;
import org.apollo.game.event.impl.SendNPCHeadEvent;
import org.apollo.game.event.impl.CreateObjectEvent;
import org.apollo.game.event.impl.SendPlayerHeadEvent;
import org.apollo.game.event.impl.SendProjectileEvent;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.event.impl.SetInterfaceModelEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.SetSidebarEvent;
import org.apollo.game.event.impl.SpecialBarEvent;
import org.apollo.game.event.impl.SwitchTabInterfaceEvent;
import org.apollo.game.event.impl.UpdateInventoryEvent;
import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.event.impl.UpdateSkillEvent;
import org.apollo.game.event.impl.UpdateSlottedItemsEvent;
import org.apollo.game.event.impl.WalkableInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceModelMoodEvent;
import org.apollo.game.event.impl.SetInterfaceComponentEvent;
import org.apollo.game.event.impl.SetInterfaceItemModelEvent;
import org.apollo.game.event.impl.SetInterfaceNpcModelEvent;
import org.apollo.game.event.impl.SetInterfacePlayerModelEvent;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;
/**
 * An implementation of {@link Release} for the 317 protocol.
 * @author Graham
 */
public final class Release317 extends Release {

    /**
     * The incoming packet lengths array.
     */
    public static final int[] PACKET_LENGTHS = {
        0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
        0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
        0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
        0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
        2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
        0, 0, 0, 12, 0, 0, 0, 0, 8, 0, // 50
        0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
        6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
        0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
        0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
        0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
        0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
        1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
        0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
        0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
        0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
        0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
        0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
        0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
        2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
        4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
        0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
        1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
        0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
        0, 0, 6, 6, 0, 0, // 250
    };

    /**
     * Creates and initializes this release.
     */
    public Release317() {
        super(317, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
        init();
    }

    /**
     * Initializes this release by registering encoders and decoders.
     */
    private void init() {
        // register decoders
        WalkEventDecoder walkEventDecoder = new WalkEventDecoder();
        register(248, walkEventDecoder);
        register(164, walkEventDecoder);
        register(98, walkEventDecoder);
        
        BuildFriendsEventDecoder privateChatDecoder = new BuildFriendsEventDecoder();
        register(215, privateChatDecoder);
        register(188, privateChatDecoder);
        register(133, privateChatDecoder);
        register(74, privateChatDecoder);
        
        register(0, new KeepAliveEventDecoder());
        register(4, new ChatEventDecoder());
        register(16, new ThirdItemOptionEventDecoder());
        register(17, new SecondNPCActionEventDecoder());
        register(21, new ThirdNPCActionEventDecoder());
        register(40, new DialogueContinueEventDecoder());
        register(41, new SecondItemOptionEventDecoder());
        register(43, new ThirdItemActionEventDecoder());
        register(53, new ItemOnItemEventDecoder());
        register(70, new ThirdObjectActionEventDecoder());
        register(72, new AttackNPCActionEventDecoder());
        register(73, new AttackPlayerEventDecoder());
        register(75, new FourthItemOptionEventDecoder());
        register(87, new FifthItemOptionEventDecoder());
        register(101, new CharacterDesignEventDecoder());
        register(103, new CommandEventDecoder());
        register(117, new SecondItemActionEventDecoder());
        register(122, new FirstItemOptionEventDecoder());
        register(126, new PrivateChatEventDecoder());
        register(129, new FourthItemActionEventDecoder());
        register(130, new ClosedInterfaceEventDecoder());
        register(132, new FirstObjectActionEventDecoder());
        register(135, new FifthItemActionEventDecoder());
        register(139, new AnswerTradeEventDecoder());
        register(145, new FirstItemActionEventDecoder());
        register(153, new ThirdPlayerActionDecoder());
        register(155, new FirstNPCActionEventDecoder());
        register(185, new ButtonEventDecoder());
        register(192, new ItemOnObjectEventDecoder());
        register(208, new EnteredAmountEventDecoder());
        register(214, new SwitchItemEventDecoder());
        register(236, new PickupItemEventDecoder());
        register(237, new MagicOnItemEventDecoder());
        register(252, new SecondObjectActionEventDecoder());

        // register encoders
        register(PlayerMenuEvent.class, new PlayerMenuEventEncoder());
        register(SetInterfaceComponentEvent.class, new SetInterfaceComponentEventEncoder());
	register(SetInterfaceItemModelEvent.class, new SetInterfaceItemModelEventEncoder());
	register(SetInterfaceNpcModelEvent.class, new SetInterfaceNpcModelEventEncoder());
	register(SetInterfacePlayerModelEvent.class, new SetInterfacePlayerModelEventEncoder());
	register(SetInterfaceModelMoodEvent.class, new SetInterfaceModelMoodEventEncoder());
        register(PlaySongEvent.class, new PlaySongEventEncoder());
        register(SendPlayerHeadEvent.class, new SendPlayerHeadEventEncoder());
        register(ChatHeadAnimationEvent.class, new ChatHeadAnimationEventEncoder());
        register(SendNPCHeadEvent.class, new SendNPCHeadEventEncoder());
        register(OpenInterfaceDialogueEvent.class, new OpenInterfaceDialogueEventEncoder());
        register(WalkableInterfaceEvent.class, new WalkableInterfaceEventEncoder());
        register(ObjectAnimationEvent.class, new ObjectAnimationEventEncoder());
        register(IdAssignmentEvent.class, new IdAssignmentEventEncoder());
        register(RegionChangeEvent.class, new RegionChangeEventEncoder());
        register(ServerMessageEvent.class, new ServerMessageEventEncoder());
        register(SetInterfaceTextEvent.class, new SetInterfaceTextEventEncoder());
        register(SetInterfaceModelEvent.class, new SetInterfaceModelEventEncoder());
        register(NPCSynchronizationEvent.class, new NPCSynchronizationEventEncoder());
        register(PlayerSynchronizationEvent.class, new PlayerSynchronizationEventEncoder());
        register(OpenInterfaceEvent.class, new OpenInterfaceEventEncoder());
        register(OpenChatInterfaceEvent.class, new OpenChatInterfaceEventEncoder());
        register(CloseInterfaceEvent.class, new CloseInterfaceEventEncoder());
        register(SwitchTabInterfaceEvent.class, new SwitchTabInterfaceEventEncoder());
        register(DisplayTabInterfaceEvent.class, new DisplayTabInterfaceEventEncoder());
        register(LogoutEvent.class, new LogoutEventEncoder());
        register(UpdateItemsEvent.class, new UpdateItemsEventEncoder());
        register(UpdateSlottedItemsEvent.class, new UpdateSlottedItemsEventEncoder());
        register(UpdateSkillEvent.class, new UpdateSkillEventEncoder());
        register(OpenInterfaceSidebarEvent.class, new OpenInterfaceSidebarEventEncoder());
        register(EnterAmountEvent.class, new EnterAmountEventEncoder());
        register(SendProjectileEvent.class, new SendProjectileEventEncoder());
        register(PositionEvent.class, new PositionEventEncoder());
        register(CreateObjectEvent.class, new CreateObjectEventEncoder());
        register(SendConfigEvent.class, new SendConfigEventEncoder());
        register(SendGroundItemEvent.class, new SendGroundItemEventEncoder());
        register(RemoveGroundItemEvent.class, new RemoveGroundItemEventEncoder());
        register(SetSidebarEvent.class, new SetSidebarEventEncoder());
        register(UpdateInventoryEvent.class, new UpdateInventoryEncoder());
        register(PrivateChatLoadedEvent.class, new PrivateChatLoadedEventEncoder());
        register(RemoveObjectEvent.class, new RemoveObjectEventEncoder());
        register(SpecialBarEvent.class, new SpecialBarEncoder());
        register(BuildFriendsEvent.class, new BuildFriendsEventEncoder());
        register(PrivateChatEvent.class, new PrivateChatEventEncoder());
    }
}
