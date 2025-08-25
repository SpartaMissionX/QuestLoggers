package com.missionx.questloggers.domain.test.character;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.service.BossService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final BossService bossService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void createUserAndChar() {
        List<User> userList = new ArrayList<>();
        String password = "1234Qwer!";
        String encodedPassword = passwordEncoder.encode(password);
        for (int i = 1; i <= 1000; i++) {
            User user = new User("example" + i + "@naver.com", encodedPassword, "test-f7d677ebd9e2982d9bbeb5d0f21cc6bd773457cc480edf8201a7ca23dce6402efe8d04e6d233bd35cf2fabdeb93fb0d" + i, Role.USER);
            userList.add(user);
        }
        userRepository.saveAll(userList);
        for (User user : userList) {
            createChar(user);
        }
    }

    @Transactional
    public void createChar(User thuser) {
        List<Character> characterList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String ocid = "2af5b561ecfdae13abfca566fced8ffdefe8d04e6d233bd35cf2fabdeb93fb0d" + i + thuser.getEmail();
            String charName = "길동이" + i;
            String worldName = "스카니아";
            String charClass = "모험가";
            int charLevel = 200;
            Character character = new Character(thuser, ocid, charName, worldName, charClass, charLevel);
            characterList.add(character);
        }
        characterRepository.saveAll(characterList);
    }

    @Transactional
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

    @Transactional
    public void createPost() {
        Random random = new Random();
        List<Post> postList = new ArrayList<>();

        List<User> userList = userSupportService.findAllUsers();
        List<Character> characterList = characterSupportService.findAllCharacter();

        List<Boss> bosses = bossSupportService.findAllBoss();

        for (int o = 1; o <= 100; o++) {
            for (User user : userList) {
                Character character = characterSupportService.findById(user.getOwnerCharId());
                Boss boss = bosses.get(random.nextInt(bosses.size()));

                String title = boss.getBossName() + "잡기 [" + user.getOwnerCharName() + "의 게시물]";
                String content = "내용";

                Difficulty[] difficulties = Difficulty.values();
                int randomIndex = random.nextInt(difficulties.length);
                Difficulty randomDifficulty = difficulties[randomIndex];

                int partySize = random.nextInt(4) + 2;

                postList.add(new Post(title, content, character, boss, randomDifficulty, partySize));
            }
        }
        postRepository.saveAll(postList);
    }
}
