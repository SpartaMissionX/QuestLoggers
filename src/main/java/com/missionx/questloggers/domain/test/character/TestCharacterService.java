package com.missionx.questloggers.domain.test.character;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.service.BossSupportService;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.character.service.CharacterSupportService;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.enums.Difficulty;
import com.missionx.questloggers.domain.post.repository.PostRepository;
import com.missionx.questloggers.domain.post.service.PostSupportService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.enums.Role;
import com.missionx.questloggers.domain.user.repository.UserRepository;
import com.missionx.questloggers.domain.user.service.UserSupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestCharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final UserSupportService userSupportService;
    private final CharacterSupportService characterSupportService;
    private final BossSupportService bossSupportService;
    private final PostRepository postRepository;

    public void createUserAndChar() {
        for (int i = 1; i <= 10000; i++) {
            User user = new User("example" + i + "@naver.com", "1234Qwer!", "test-f7d677ebd9e2982d9bbeb5d0f21cc6bd773457cc480edf8201a7ca23dce6402efe8d04e6d233bd35cf2fabdeb93fb0d" + i, Role.USER);
            userRepository.save(user);
            createChar(user);
            setUserOwnerCharId(user);
        }
    }


    public void createChar(User thuser) {
        for (int i = 1; i <= 10; i++) {
            String ocid = "2af5b561ecfdae13abfca566fced8ffdefe8d04e6d233bd35cf2fabdeb93fb0d" + i + thuser.getEmail();
            String charName = "길동이" + i;
            String worldName = "스카니아";
            String charClass = "모험가";
            int charLevel = 200;
            characterRepository.save(new Character(thuser, ocid, charName, worldName, charClass, charLevel));
        }
    }

    public void setUserOwnerCharId(User user) {

        User user1 = userSupportService.findUserById(user.getId());
        if (user1.getOwnerCharId() == null) {
            List<Character> foundChar = characterSupportService.findByUser(user1);
            Character randomChar = foundChar.get(new Random().nextInt(foundChar.size()));
            randomChar.updateOwnerChar(true);
            characterRepository.save(randomChar);
            user1.updateOwnerChar(randomChar.getId(), randomChar.getCharName());
            userRepository.save(user1);
        }
    }

    public void createPost() {
        Random random = new Random();
        for (int o = 1; o <= 90; o++) {
            for (int i = 1; i <= 10000; i++) {
                Long userId = Long.valueOf(i);
                User user = userSupportService.findUserById(userId);
                Character character = characterSupportService.findByMainCharId(user.getOwnerCharId());
                int randomBossId = random.nextInt(14) + 1;
                Long bossId = Long.valueOf(randomBossId);
                Boss boss = bossSupportService.findById(bossId);
                String title = boss.getBossName() + "잡기 [" + user.getOwnerCharName() + "의 게시물]";
                String content = "내용";

                Difficulty[] difficulties = Difficulty.values();
                int randomIndex = ThreadLocalRandom.current().nextInt(difficulties.length);
                Difficulty randomDifficulty = difficulties[randomIndex];

                int partySize = random.nextInt(4) + 2;

                Post post = new Post(title, content, character, boss, randomDifficulty, partySize);
                postRepository.save(post);
            }
        }

    }
}
