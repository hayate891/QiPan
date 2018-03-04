package io.nibby.qipan.sound;

import javafx.scene.media.AudioClip;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sound {

    public enum Type {
        BOWL_OPEN,
        BOWL_CLOSE,
        STONE_RUSTLE_WHITE,
        STONE_RUSTLE_BLACK,
        STONE_PLACE_SINGLE,
        STONE_PLACE_ADJACENT,
        STONE_PLACE_SNAP,
        STONE_COLLISION_WOBBLE,
        STONE_COLLISION_WOBBLE_BIG,
        STONE_CAPTURE_SINGLE,
        STONE_CAPTURE_DOUBLE,
        STONE_CAPTURE_TRIPLE,
        STONE_CAPTURE_MULTIPLE,
        STONE_PUTBACK
    }

    private static final Map<Type, List<AudioClip>> AUDIO_DB = new HashMap<>();

    static {
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock1.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock2.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock3.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock4.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock5.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock6.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock7.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock8.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock9.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock10.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock11.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock12.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock13.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock14.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock15.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock16.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock17.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock18.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE, "/sound/biconvex_stones/adj_knock19.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE_BIG, "/sound/biconvex_stones/big_knock1.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE_BIG, "/sound/biconvex_stones/big_knock2.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE_BIG, "/sound/biconvex_stones/big_knock3.aiff");
        loadAudio(Type.STONE_COLLISION_WOBBLE_BIG, "/sound/biconvex_stones/big_knock4.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack1.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack2.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack3.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack4.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack5.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack6.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack7.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack8.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack9.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack10.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack11.aiff");
        loadAudio(Type.STONE_PLACE_SINGLE, "/sound/biconvex_stones/clack12.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj1.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj2.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj3.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj4.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj5.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj6.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj7.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj8.aiff");
        loadAudio(Type.STONE_PLACE_ADJACENT, "/sound/biconvex_stones/clack_adj9.aiff");
        loadAudio(Type.STONE_PUTBACK, "/sound/biconvex_stones/putback1.aiff");
        loadAudio(Type.STONE_PUTBACK, "/sound/biconvex_stones/putback2.aiff");
        loadAudio(Type.STONE_PUTBACK, "/sound/biconvex_stones/putback3.aiff");
        loadAudio(Type.STONE_PUTBACK, "/sound/biconvex_stones/putback4.aiff");
        loadAudio(Type.STONE_PUTBACK, "/sound/biconvex_stones/putback5.aiff");
        loadAudio(Type.STONE_PUTBACK, "/sound/biconvex_stones/putback6.aiff");
        loadAudio(Type.STONE_PUTBACK, "/sound/biconvex_stones/putback7.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black1.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black2.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black3.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black4.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black5.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black6.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black7.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black8.aiff");
        loadAudio(Type.STONE_RUSTLE_BLACK, "/sound/biconvex_stones/rustle_black9.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white1.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white2.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white3.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white4.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white5.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white6.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white7.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white8.aiff");
        loadAudio(Type.STONE_RUSTLE_WHITE, "/sound/biconvex_stones/rustle_white9.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap1.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap2.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap3.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap4.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap5.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap6.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap7.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap8.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap9.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap10.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap11.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap12.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap13.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap14.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap15.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap16.aiff");
        loadAudio(Type.STONE_PLACE_SNAP, "/sound/biconvex_stones/snap17.aiff");
    }

    public static AudioClip loadAudio(Type type, String res) {
        AudioClip clip = null;
        try {
            clip = new AudioClip(Sound.class.getResource(res).toURI().toString());
            AUDIO_DB.putIfAbsent(type, new ArrayList<>());
            AUDIO_DB.get(type).add(clip);
            return clip;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void playRandom(Type type) {
        AUDIO_DB.get(type).get((int) (Math.random() * AUDIO_DB.get(type).size())).play();
    }

    public static void playMove(int color, int nearby, boolean snap, boolean bigCollision, ActionCallback callback) {
        new Thread(() -> {
//            playRandom(color == Stone.BLACK ? Type.STONE_RUSTLE_BLACK : Type.STONE_RUSTLE_WHITE);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            if (nearby == 0 && !bigCollision)
                playRandom(Type.STONE_PLACE_SINGLE);
            else if (snap)
                playRandom(Type.STONE_PLACE_SNAP);
            else if (nearby == 1)
                playRandom(Type.STONE_PLACE_ADJACENT);
            callback.performAction();

            if (nearby > 0) {
                if (!bigCollision) {
                    for (int i = 0; i < nearby; i++) {
                        playRandom(Type.STONE_COLLISION_WOBBLE);
                        try {
                            Thread.sleep((int) (Math.random() * 25 + 25));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    playRandom(Type.STONE_COLLISION_WOBBLE_BIG);
                }

            }
        }).start();
    }

    public interface ActionCallback {
        void performAction();
    }
}
