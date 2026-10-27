package com.example.data.preseed

import com.example.data.local.BookEntity
import com.example.data.local.BookPageEntity
import com.example.data.model.BookCategory
import com.example.data.model.BookSize

object InitialBooksData {

    data class SeedBook(
        val entity: BookEntity,
        val pages: List<BookPageEntity>
    )

    fun getInitialSeedData(): List<SeedBook> {
        val list = mutableListOf<SeedBook>()

        // 1. The Tale of Peter Rabbit (Kids, Small, Free starter, 5 Coins reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "peter_rabbit",
                    title = "The Tale of Peter Rabbit",
                    author = "Beatrix Potter",
                    description = "Once upon a time there were four little Rabbits: Flopsy, Mopsy, Cotton-tail, and Peter. A timeless countryside adventure about a mischievous bunny.",
                    categoryName = BookCategory.KIDS.name,
                    genre = "Animal Adventure",
                    license = "Public Domain (Project Gutenberg #14838)",
                    source = "https://www.gutenberg.org/ebooks/14838",
                    bookSize = BookSize.SMALL.name,
                    coinPrice = 0,
                    completionReward = 5,
                    coverColorHex = 0xFF4ADE80,
                    coverIconName = "Pets",
                    totalPages = 4,
                    isUnlockedByDefault = true
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "peter_rabbit",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: Into the Woods",
                        content = "Once upon a time there were four little Rabbits, and their names were—\nFlopsy, Mopsy, Cotton-tail, and Peter.\n\nThey lived with their Mother in a sand-bank, underneath the root of a very big fir-tree.\n\n\"Now, my dears,\" said old Mrs. Rabbit one morning, \"you may go into the fields or down the lane, but don't go into Mr. McGregor's garden: your Father had an accident there; he was put in a pie by Mrs. McGregor.\""
                    ),
                    BookPageEntity(
                        bookId = "peter_rabbit",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Garden Gate",
                        content = "\"Now run along, and don't get into mischief. I am going out.\"\n\nThen old Mrs. Rabbit took a basket and her umbrella, and went through the wood to the baker's. She bought a loaf of brown bread and five currant buns.\n\nFlopsy, Mopsy, and Cotton-tail, who were good little bunnies, went down the lane to gather blackberries.\n\nBut Peter, who was very naughty, ran straight away to Mr. McGregor's garden, and squeezed under the gate!"
                    ),
                    BookPageEntity(
                        bookId = "peter_rabbit",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: The Gooseberry Net",
                        content = "First he ate some lettuces and some French beans; and then he ate some radishes; and then, feeling rather sick, he went to look for some parsley.\n\nBut round the end of a cucumber frame, whom should he meet but Mr. McGregor!\n\nMr. McGregor was on his hands and knees planting out young cabbages, but he jumped up and ran after Peter, waving a rake and calling out, \"Stop thief!\"\n\nPeter was most dreadfully frightened; he rushed all over the garden, for he had forgotten the way back to the gate."
                    ),
                    BookPageEntity(
                        bookId = "peter_rabbit",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: Safe at Home",
                        content = "Peter gave up himself for lost, and shed big tears; but his sobs were overheard by some friendly sparrows, who flew to him in great excitement, and implored him to exert himself.\n\nPeter wriggled out of his jacket just in time, leaving his little blue coat among the gooseberry bushes.\n\nHe slipped under the gate safely at last! Peter never stopped running till he got home to the big fir-tree. His mother put him to bed, and made some chamomile tea: \"One table-spoonful to be taken at bed-time!\"\n\nFlopsy, Mopsy, and Cotton-tail had bread and milk and blackberries for supper, while Peter rested, dreaming of new adventures."
                    )
                )
            )
        )

        // 2. Alice's Adventures in Wonderland (Kids, Medium, 50 coins, 15 coins reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "alice_wonderland",
                    title = "Alice's Adventures in Wonderland",
                    author = "Lewis Carroll",
                    description = "Tumble down the rabbit hole with Alice into a whimsical wonderland of talking rabbits, tea parties, and queens.",
                    categoryName = BookCategory.KIDS.name,
                    genre = "Fantasy & Wonder",
                    license = "Public Domain (Project Gutenberg #11)",
                    source = "https://www.gutenberg.org/ebooks/11",
                    bookSize = BookSize.MEDIUM.name,
                    coinPrice = 50,
                    completionReward = 15,
                    coverColorHex = 0xFFEC4899,
                    coverIconName = "AutoStories",
                    totalPages = 5,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "alice_wonderland",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: Down the Rabbit-Hole",
                        content = "Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, 'and what is the use of a book,' thought Alice, 'without pictures or conversations?'\n\nSuddenly a White Rabbit with pink eyes ran close by her.\n\nThere was nothing so very remarkable in that; nor did Alice think it so very much out of the way to hear the Rabbit say to itself, 'Oh dear! Oh dear! I shall be late!' But when the Rabbit actually took a watch out of its waistcoat-pocket, Alice started to her feet!"
                    ),
                    BookPageEntity(
                        bookId = "alice_wonderland",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Pool of Tears",
                        content = "Down, down, down. Would the fall never come to an end?\n\n'I wonder how many miles I've fallen by this time?' she said aloud. 'I must be getting somewhere near the centre of the earth.'\n\nDown went Alice, and bump! bump! down she came upon a heap of sticks and dry leaves, and the fall was over.\n\nBefore her was another long passage, and the White Rabbit was still in sight, hurrying down it. Alice was not a moment to be lost: away went Alice like the wind, and was just in time to hear it say, as it turned a corner, 'Oh my ears and whiskers, how late it's getting!'"
                    ),
                    BookPageEntity(
                        bookId = "alice_wonderland",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: Drink Me and Eat Me",
                        content = "Alice found a little three-legged table, all made of solid glass; there was nothing on it except a tiny golden key, and Alice's first thought was that it might belong to one of the doors of the hall.\n\nShe opened a door leading into a small passage; she knelt down and looked along the passage into the loveliest garden you ever saw.\n\nOn the glass table sat a little bottle with a paper label: 'DRINK ME' beautifully printed in large letters. Alice ventured to taste it, and, finding it very nice (a sort of mixed flavour of cherry-tart, custard, pineapple, and roast turkey), she very soon finished it off. 'What a curious feeling!' said Alice; 'I must be shutting up like a telescope!'"
                    ),
                    BookPageEntity(
                        bookId = "alice_wonderland",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The Mad Tea-Party",
                        content = "There was a table set out under a tree in front of the house, and the March Hare and the Hatter were having tea at it: a Dormouse was sitting between them, fast asleep.\n\n'No room! No room!' they cried out when they saw Alice coming.\n\n'There's plenty of room!' said Alice indignantly, and she sat down in a large arm-chair at one end of the table.\n\n'Have some juice,' the March Hare said in an encouraging tone.\nAlice looked all round the table, but there was nothing on it but tea. 'I don't see any juice,' she remarked.\n'There isn't any,' said the March Hare.\n'Then it wasn't very civil of you to offer it,' said Alice angrily."
                    ),
                    BookPageEntity(
                        bookId = "alice_wonderland",
                        pageNumber = 5,
                        chapterTitle = "Chapter 5: The Queen's Garden & Awaking",
                        content = "A large rose-tree stood near the entrance of the garden: the roses growing on it were white, but there were three gardeners at it, busily painting them red.\n\nSuddenly a trumpet sounded: 'The King and Queen of Hearts!'\n\nThe Queen turned angrily to Alice. 'Who is this?'\n\n'My name is Alice, so please your Majesty,' said Alice very politely; but she thought to herself, 'Why, they're only a pack of cards, after all. I needn't be afraid of them!'\n\nAt this the whole pack rose up into the air, and came flying down upon her! Alice gave a little scream, half of fright and half of anger, and found herself lying on the bank, with her head in the lap of her sister, who was gently brushing away some dead leaves that had fluttered down from the trees upon her face.\n\n'Wake up, Alice dear!' said her sister. 'What a curious dream it was!' whispered Alice with a bright smile."
                    )
                )
            )
        )

        // 3. The Jungle Book (Kids, Large, 100 coins, 50 coins reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "jungle_book",
                    title = "The Jungle Book",
                    author = "Rudyard Kipling",
                    description = "Follow Mowgli the man-cub raised by wolves in the wild jungles of India, taught the law of the jungle by Baloo the bear and Bagheera the panther.",
                    categoryName = BookCategory.KIDS.name,
                    genre = "Jungle Adventure",
                    license = "Public Domain (Project Gutenberg #236)",
                    source = "https://www.gutenberg.org/ebooks/236",
                    bookSize = BookSize.LARGE.name,
                    coinPrice = 100,
                    completionReward = 50,
                    coverColorHex = 0xFF059669,
                    coverIconName = "Forest",
                    totalPages = 6,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "jungle_book",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: Mowgli's Brothers",
                        content = "It was seven o'clock of a very warm evening in the Seeonee hills when Father Wolf woke up from his day's rest, scratched himself, yawned, and spread out his paws one after another to get rid of the sleepy feeling in their tips.\n\nMother Wolf lay with her big gray nose dropped across her four tumbling, squealing cubs, and the moon shone into the mouth of the cave where they all lived.\n\n'Augrh!' said Father Wolf. 'It is time to hunt again.' He was going to spring down hill when a little shadow with a bushy tail crossed the threshold and whined: 'Good luck go with you, O Chief of the Wolves.'"
                    ),
                    BookPageEntity(
                        bookId = "jungle_book",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Man-Cub",
                        content = "Father Wolf listened, and below in the valley he heard the dry, angry, snarly whine of a tiger who has caught nothing.\n\n'Shere Khan,' said Father Wolf. 'The fool! To begin a night's work with that noise! Does he think that our buck are like his fat Waingunga bullocks?'\n\nThe bushes rustled a little in the thicket, and Father Wolf dropped with his haunches under him, ready for his leap. Then, if you had been watching, you would have seen the most wonderful thing in the world—the wolf checked in mid-spring.\n\n'Man!' he snapped. 'A man's cub. Look!'\n\nDirectly in front of him, holding on by a low branch, stood a naked brown baby who could just walk—as soft and as dimpled a little atom as ever came to a wolf's cave at night."
                    ),
                    BookPageEntity(
                        bookId = "jungle_book",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: Mother Wolf's Courage",
                        content = "'How little! How naked, and—how bold!' said Mother Wolf softly. The baby was pushing his way between the cubs to get close to the warm hide.\n\nShere Khan's great square head and shoulders were thrust into the cave entrance.\n\n'My quarry. A man's cub went this way,' said Shere Khan. 'His parents have fled. Give it to me.'\n\nMother Wolf shook herself clear of the cubs and sprang forward, her eyes, like two green moons in the darkness, facing the dreadful eyes of Shere Khan. 'The man's cub is mine, Shere Khan! He shall live to run with the Pack and to hunt with the Pack; and in the end, look you, hunter of little naked cubs—frog-eater—fish-killer—he shall hunt thee! Now get out!'"
                    ),
                    BookPageEntity(
                        bookId = "jungle_book",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The Council Rock",
                        content = "The Law of the Jungle laid down that any wolf may, when he marries, withdraw from the Pack; but as soon as his cubs are old enough to stand on their feet he must bring them to the Pack Council, which is generally held once a month at full moon, in order that the other wolves may identify them.\n\nAkela, the great gray Lone Wolf, who led all the Pack by strength and cunning, lay out at full length on his rock.\n\n'Look well, O Wolves! What have the Free People to do with the man-cub?'\n\nThen the only other creature allowed at the Pack Council—Baloo, the sleepy brown bear who teaches the wolf cubs the Law of the Jungle—rose upon his hind quarters and spoke: 'I speak for the man-cub. There is no harm in him. I will teach him!'"
                    ),
                    BookPageEntity(
                        bookId = "jungle_book",
                        pageNumber = 5,
                        chapterTitle = "Chapter 5: Bagheera's Gift",
                        content = "A black shadow dropped down into the circle. It was Bagheera the Black Panther, inky black all over, but with the panther markings showing up under certain lights like the pattern of watered silk. Everybody knew Bagheera, and nobody cared to cross his path; for he was as cunning as Tabaqui, as bold as the wild buffalo, and as reckless as the wounded elephant. But he had a voice as soft as wild honey dripping from a tree.\n\n'To kill a naked cub is shame,' said Bagheera. 'Besides, he may make better sport for you when he is grown. Baloo has spoken in his behalf. Now to Baloo's word I will add one bull, and a fat one, newly killed, less than half a mile from here, if ye will accept the man-cub according to the Law.'\n\n'Let him run with the Pack!' cried the young wolves."
                    ),
                    BookPageEntity(
                        bookId = "jungle_book",
                        pageNumber = 6,
                        chapterTitle = "Chapter 6: Master of the Jungle",
                        content = "And that is how Mowgli was entered into the Seeonee Wolf Pack for the price of a bull and on Baloo's good word.\n\nMowgli grew up with the cubs, though they, of course, were grown wolves almost before he was a child. And Father Wolf taught him his business, and the meaning of things in the jungle, till every rustle in the grass, every breath of the warm night air, every note of the owls above his head meant just as much to him as the work of his office means to a business man.\n\nWhen he was not learning he sat out in the sun and slept, and ate, and went back to sleep again; when he felt dirty or hot he swam in the forest pools; and when he wanted honey he climbed up into the trees as easily as any monkey.\n\nBagheera would lie on a branch and purr: 'Thou art a man-cub, Little Brother. And there is no beast in all the jungle that can look thee between the eyes.' Mowgli smiled, standing tall as the guardian of the green wild."
                    )
                )
            )
        )

        // 4. The Strange Case of Dr. Jekyll and Mr. Hyde (Horror & Mystery, Medium, 50 coins, 15 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "jekyll_hyde",
                    title = "Dr. Jekyll and Mr. Hyde",
                    author = "Robert Louis Stevenson",
                    description = "An eerie mystery unfolds in the foggy gaslit streets of Victorian London, as lawyer Utterson investigates a sinister figure named Edward Hyde.",
                    categoryName = BookCategory.HORROR_MYSTERY.name,
                    genre = "Gothic Mystery",
                    license = "Public Domain (Project Gutenberg #43)",
                    source = "https://www.gutenberg.org/ebooks/43",
                    bookSize = BookSize.MEDIUM.name,
                    coinPrice = 50,
                    completionReward = 15,
                    coverColorHex = 0xFF6366F1,
                    coverIconName = "Visibility",
                    totalPages = 5,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "jekyll_hyde",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: Story of the Door",
                        content = "Mr. Utterson the lawyer was a man of a rugged countenance, that was never lighted by a smile; cold, scanty and embarrassed in discourse; yet somehow lovable.\n\nIt was his custom on the Sunday walk with his kinsman, Mr. Richard Enfield, to stroll down a certain quiet, respectable street in London.\n\nThere stood a sinister two-story building that bore every mark of prolonged neglect. It was equipped with neither bell nor knocker, and the door was blistered and distained.\n\n'Did you ever remark that door?' asked Enfield. 'It is connected in my mind with a very strange story. A man named Hyde trampled a young child in the street, yet produced a cheque signed by a reputable gentleman to make amends!'"
                    ),
                    BookPageEntity(
                        bookId = "jekyll_hyde",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: Search for Mr. Hyde",
                        content = "That evening, Mr. Utterson went to his study and unlocked his safe. He took out the will of Dr. Henry Jekyll. In case of Jekyll's decease or unexplained absence for more than three months, all possessions were to pass to 'Edward Hyde.'\n\n'I thought it was madness,' muttered Utterson, 'and now I begin to fear it is disgrace.'\n\nNight after night, Utterson began to haunt the mysterious door. At last, on a frosty night, he encountered a small, pale man who gave an odd impression of deformity without any nameable malformation.\n\n'Mr. Hyde, I think?' Utterson whispered. Hyde shrank back, hissing through his teeth."
                    ),
                    BookPageEntity(
                        bookId = "jekyll_hyde",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: The Doctor's Secret",
                        content = "A fortnight later, Dr. Jekyll gave a pleasant dinner to five or six old cronies, all intelligent, reputable men. Utterson stayed behind after the others had departed.\n\n'Jekyll,' said Utterson, 'I have been learning something of young Hyde.'\n\nThe large, handsome face of Dr. Jekyll grew pale to the very lips, and there came a blackness about his eyes.\n\n'Utterson,' said the doctor, 'my position is a very strange—a very strange one. It is one of those affairs that cannot be mended by talking. The moment I choose, I can be rid of Mr. Hyde. I give you my hand upon that; and I thank you again and again.'"
                    ),
                    BookPageEntity(
                        bookId = "jekyll_hyde",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The Last Night",
                        content = "Nearly a year later, Poole, Jekyll's loyal butler, arrived at Utterson's door in trembling terror.\n\n'Mr. Utterson, there is something wrong. The doctor has been locked in his cabinet for eight days. The voice that answers through the wood is not his voice!'\n\nTogether they hurried through the blustery wind to the laboratory. Utterson called out: 'Jekyll, I demand to see you!'\n\nA strange, rasping voice cried: 'Utterson, for God's sake, have mercy!'\n\n'That won't do!' shouted Poole. He swung the heavy axe, and after five furious blows, the red baize door splintered open!"
                    ),
                    BookPageEntity(
                        bookId = "jekyll_hyde",
                        pageNumber = 5,
                        chapterTitle = "Chapter 5: The Dual Nature of Man",
                        content = "Inside the quiet room lay a sealed packet addressed to Utterson in Dr. Jekyll's own handwriting.\n\nIt explained the terrible truth: Henry Jekyll had concocted a chemical draught that separated the higher virtues from the darker impulses of the human soul. When he drank it, he transformed physically and morally into the cruel Edward Hyde!\n\nYet as time passed, the dark side grew too powerful to control, taking over even during sleep without the potion.\n\n'Man is not truly one, but truly two,' wrote the doomed doctor in warning to humanity. Utterson closed the manuscript under the midnight lamp, forever changed by the revelation."
                    )
                )
            )
        )

        // 5. The Adventures of Sherlock Holmes (Horror & Mystery, Large, 100 coins, 50 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "sherlock_holmes",
                    title = "The Adventures of Sherlock Holmes",
                    author = "Arthur Conan Doyle",
                    description = "Join Dr. John Watson and the world's greatest consulting detective, Sherlock Holmes, as they solve the baffling case of A Scandal in Bohemia.",
                    categoryName = BookCategory.HORROR_MYSTERY.name,
                    genre = "Detective Fiction",
                    license = "Public Domain (Project Gutenberg #1661)",
                    source = "https://www.gutenberg.org/ebooks/1661",
                    bookSize = BookSize.LARGE.name,
                    coinPrice = 100,
                    completionReward = 50,
                    coverColorHex = 0xFF7C3AED,
                    coverIconName = "Search",
                    totalPages = 5,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "sherlock_holmes",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: The Woman",
                        content = "To Sherlock Holmes she is always THE woman. I have seldom heard him mention her under any other name. In his eyes she eclipses and predominates the whole of her sex.\n\nOne night—it was on the twentieth of March, 1888—I was returning from a journey to a patient, when my way led me through Baker Street.\n\nAs I passed the well-remembered door, I looked up. His tall, spare figure passed twice in a dark silhouette against the blind. He was pacing the room swiftly, eagerly, with his head sunk upon his chest and his hands clasped behind him. To me, who knew his every mood and habit, his attitude and manner told their own story. He was at work again."
                    ),
                    BookPageEntity(
                        bookId = "sherlock_holmes",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Masked Royal",
                        content = "A slow and heavy step was heard upon the stairs. The door opened, and a man entered who could not have been less than six feet six inches in height, with the chest and limbs of a Hercules.\n\nHe wore a heavy fur coat and a black vizard mask extending down past his cheekbones.\n\n'You had my note?' he asked with a deep, German accent. 'I am Count von Kramm.'\n\n'Pray consult me with frankness, Your Majesty,' said Holmes quietly, leaning back in his armchair. 'The King of Bohemia need not wear a mask before me.'\n\nThe visitor sprang from his chair and tore the mask from his face! 'You are right! Irene Adler possesses a photograph that could ruin my upcoming royal marriage. I must recover it!'"
                    ),
                    BookPageEntity(
                        bookId = "sherlock_holmes",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: The Disguise",
                        content = "Next afternoon, Holmes appeared disguised as an amiable, out-of-work groom with whiskers and a swagger.\n\n'Watson,' he laughed, 'Irene Adler is the daintiest thing under a bonnet on this planet! And more than that—she was married this morning to Godfrey Norton at the Church of St. Monica, and I was dragged in to be their official witness!'\n\n'And what about the photograph?' I asked.\n\n'It is hidden in her villa in St. John's Wood. Tonight, my dear Watson, you will help me locate the exact hiding place. When I signal through the window, you will raise an alarm of fire and toss this smoke-rocket into the sitting-room!'"
                    ),
                    BookPageEntity(
                        bookId = "sherlock_holmes",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The Fire Alarm",
                        content = "Holmes disguised himself as a venerable clergyman. When Irene Adler's carriage pulled up at her villa, a scuffle broke out on the street, and Holmes fell to the ground, apparently wounded and bleeding.\n\nWith admirable kindness, Irene ordered him carried into her sitting room.\n\nHolmes raised his hand at the window. I struck a match, lit the smoke-rocket, and hurled it through the open sash. 'Fire! Fire!' shouted twenty voices in the street.\n\nThrough the thick cloud of smoke, I saw Irene Adler rush forward like an arrow to a sliding panel behind a portrait, reach in, and snatch a cabinet photograph! That was all Holmes needed to know. She had revealed her secret vault!"
                    ),
                    BookPageEntity(
                        bookId = "sherlock_holmes",
                        pageNumber = 5,
                        chapterTitle = "Chapter 5: Outwitted by Irene Adler",
                        content = "The following morning, Holmes, Watson, and the King of Bohemia hurried to the villa to retrieve the picture.\n\nThey found the house empty! An elderly maid informed them that Madame had left England by the five o'clock train.\n\nHolmes rushed to the secret panel. The photograph was gone, but in its place lay a letter addressed to Sherlock Holmes, together with a picture of Irene Adler herself!\n\n'Dear Mr. Holmes,' the note read. 'You did it very handsomely, but as soon as the smoke cleared, I recognized you in clergyman's robes. Keep your mind at rest: I love and am loved by a better man, and your royal client is safe forever.'\n\nThe King gasped: 'What a woman! What an intellect!'\nAnd that was how a great scandal was avoided, and how the best plans of Sherlock Holmes were beaten by a woman's wit."
                    )
                )
            )
        )

        // 6. The Canterville Ghost (Comedy, Small, 25 coins, 5 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "canterville_ghost",
                    title = "The Canterville Ghost",
                    author = "Oscar Wilde",
                    description = "A hilarious and heartwarming tale of an ancient English ghost who finds himself completely outmatched by a modern, fearless American family.",
                    categoryName = BookCategory.COMEDY.name,
                    genre = "Humorous Fantasy",
                    license = "Public Domain (Project Gutenberg #14522)",
                    source = "https://www.gutenberg.org/ebooks/14522",
                    bookSize = BookSize.SMALL.name,
                    coinPrice = 25,
                    completionReward = 5,
                    coverColorHex = 0xFFF59E0B,
                    coverIconName = "SentimentVerySatisfied",
                    totalPages = 4,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "canterville_ghost",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: The Otis Family Moves In",
                        content = "When Mr. Hiram B. Otis, the American Minister, bought Canterville Chase, everyone told him he was doing a very foolish thing, as there was no doubt at all that the place was haunted.\n\n'My Lord,' answered the Minister, 'I come from a modern country where we have everything that money can buy. If there were such a thing as a ghost in Europe, we'd have it in one of our public museums at home!'\n\nWhen they arrived at the manor, Mrs. Otis noticed a dark red stain on the library floor. 'That,' said Mrs. Umney the housekeeper in a trembling whisper, 'is the blood of Lady Eleanore de Canterville, shed in 1575!'\n\n'Nonsense!' cried young Washington Otis. 'Pinkerton's Champion Stain Remover will clean that in no time!' And before the terrified housekeeper could stop him, he rubbed the centuries-old bloodstain away with a sponge."
                    ),
                    BookPageEntity(
                        bookId = "canterville_ghost",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Clanking Chains",
                        content = "At one o'clock in the morning, Mr. Otis was awakened by a curious noise in the corridor outside his room. It sounded like the clank of rusty iron chains!\n\nHe put on his slippers, opened the door, and right in front of him stood an old man of terrible aspect! His eyes were as red as burning coals, his long matted hair fell over his shoulders, and heavy rusted shackles hung from his wrists and ankles.\n\n'My dear sir,' said Mr. Otis politely, 'I really must insist on your oiling those chains! It is impossible to sleep with such a racket going on. I have brought you a small bottle of Tammany Rising Sun Lubricator for the purpose. Leave it by the stairs when you are done!'\n\nAnd with that, the American Minister closed his door and went back to bed."
                    ),
                    BookPageEntity(
                        bookId = "canterville_ghost",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: The Ghost's Humiliation",
                        content = "For several moments the Canterville ghost stood quite motionless in natural indignation! For three hundred years he had terrified dukes, frightened duchesses into fits, and caused whole regiments of guards to flee.\n\nAnd now, wretched modern mortals offered him lubricant!\n\nHe planned a magnificent revenge. Disguised as 'The Headless Earl,' he crept toward the boys' room. But as he reached the corner, the young twins suddenly jumped out with pea-shooters and peppered him with dried beans!\n\nThen a heavy pillow struck him squarely in the chest. Groaning with misery, the poor ghost melted through the wainscot and fled to his secret chamber in the attic, nursing his wounded pride."
                    ),
                    BookPageEntity(
                        bookId = "canterville_ghost",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: Peace for Sir Simon",
                        content = "A few days later, fifteen-year-old Virginia Otis found the ghost sitting by the window in the tapestry chamber, looking utterly depressed.\n\n'You are very naughty,' Virginia said gently, 'painting that library stain every morning with my watercolor paints.'\n\n'I have not slept for three hundred years,' sighed the ghost, weeping softly. 'I long for the Garden of Death, where the grass grows soft and deep, and stars shine in peace. Only a pure heart can pray for me.'\n\nVirginia took his cold hands and bravely prayed for the ancient spirit. A golden glow filled the old castle, the withered almond tree burst into fragrant white blossom, and Sir Simon finally found sweet, eternal rest. The Otis family celebrated with smiles and joy, proving that kindness is the greatest magic of all."
                    )
                )
            )
        )

        // 7. Three Men in a Boat (Comedy, Medium, 50 coins, 15 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "three_men_boat",
                    title = "Three Men in a Boat",
                    author = "Jerome K. Jerome",
                    description = "A comical boating holiday on the River Thames with George, Harris, Jerome, and Montmorency the feisty fox terrier.",
                    categoryName = BookCategory.COMEDY.name,
                    genre = "Humorous Fiction",
                    license = "Public Domain (Project Gutenberg #308)",
                    source = "https://www.gutenberg.org/ebooks/308",
                    bookSize = BookSize.MEDIUM.name,
                    coinPrice = 50,
                    completionReward = 15,
                    coverColorHex = 0xFFFB923C,
                    coverIconName = "DirectionsBoat",
                    totalPages = 4,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "three_men_boat",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: Symptoms of Everything",
                        content = "There were four of us—George, and William Samuel Harris, and myself, and Montmorency the dog. We were sitting in my room, smoking, and talking about how bad we were—bad from a medical point of view I mean, of course.\n\nWe were all feeling seedy, and we were getting quite nervous about it. Harris said he felt such extraordinary fits of giddiness come over him at times, that he hardly knew what he was doing; and then George said that he had fits of giddiness too, and hardly knew what he was doing.\n\nWith me, it was my liver that was out of order. I knew it was my liver, because I had just been reading a patent medical leaflet detailing the various symptoms by which a man could tell when his liver was out of order. I had them all!"
                    ),
                    BookPageEntity(
                        bookId = "three_men_boat",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Packing Catastrophe",
                        content = "We decided that a boating trip up the River Thames would cure all our ailments. Then came the packing.\n\nI rather pride myself on my packing. Packing is one of those many things that I feel I know more about than any other person living. I impressed the fact upon George and Harris and told them that they had better leave the whole matter entirely to me.\n\nThey fell into the suggestion with a readiness that had something uncanny about it. George put on a pipe and spread himself over the easy-chair, and Harris cocked his legs on the table and lit a cigar.\n\nThis was hardly what I intended. What I had meant, of course, was that I should boss the job, and that Harris and George should potter about under my directions!"
                    ),
                    BookPageEntity(
                        bookId = "three_men_boat",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: Montmorency's Contribution",
                        content = "Montmorency's ambition in life was to get in the way and be sworn at. If he could squirm in anywhere where he particularly was not wanted, and be a perfect nuisance, and make people mad, and have things thrown at his head, then he felt his day had not been wasted.\n\nTo get somebody to stumble over him, and curse him steadily for an hour, was his highest object; and when he had succeeded in accomplishing this, his conceit became quite unbearable.\n\nHe came and sat down on things, just when they were wanted to be packed; and he labored under the fixed belief that, whenever Harris or George reached out their hand for anything, it was a cold damp dog's nose that they wanted. He put his leg into the jam, and he worried the teaspoons, and he pretended that the lemons were rats, and got into the hamper and killed three of them before Harris could land him with the frying-pan!"
                    ),
                    BookPageEntity(
                        bookId = "three_men_boat",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The River Triumph",
                        content = "At last, we got the boat into the river. The morning sun sparkled upon the water, the willow trees bowed their gentle leaves to the surface, and the fresh breeze carried away all our imaginary ailments.\n\nHarris fell into the river while attempting to prepare a kettle of tea, George lost his banjo in the weeds, and Montmorency barked furiously at an arrogant swan who refused to be intimidated.\n\nYet when evening fell, and the golden lights of the riverside inn shone through the mist, we sat together around a steaming dish of Irish stew, laughing until our sides ached.\n\n'Well,' said George, lifting his mug, 'here's to the river, and here's to good companions!' And we all agreed that laughter was the best medicine in the world."
                    )
                )
            )
        )

        // 8. The Time Machine (Sci-Fi, Medium, 50 coins, 15 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "time_machine",
                    title = "The Time Machine",
                    author = "H.G. Wells",
                    description = "A brilliant inventor travels hundreds of thousands of years into the distant future of Earth, discovering the idyllic Eloi and the terrifying Morlocks.",
                    categoryName = BookCategory.SCIENCE_FICTION.name,
                    genre = "Time Travel Sci-Fi",
                    license = "Public Domain (Project Gutenberg #35)",
                    source = "https://www.gutenberg.org/ebooks/35",
                    bookSize = BookSize.MEDIUM.name,
                    coinPrice = 50,
                    completionReward = 15,
                    coverColorHex = 0xFF0284C7,
                    coverIconName = "HourglassBottom",
                    totalPages = 5,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "time_machine",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: The Fourth Dimension",
                        content = "The Time Traveller was expounding a recondite matter to us. His grey eyes shone and twinkled, and his usually pale face was flushed and animated.\n\n'Can a cube that does not last for any time at all have a real existence?' he asked.\n\n'Clearly,' said Filby, 'an object cannot exist without duration.'\n\n'Precisely,' said the Time Traveller. 'There are four dimensions: Length, Breadth, Thickness, and Duration. And I have constructed a machine that allows a human being to travel through Time as freely as a balloon ascends into the atmosphere!'"
                    ),
                    BookPageEntity(
                        bookId = "time_machine",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Flight Through Centuries",
                        content = "I pushed the starting lever forward. The laboratory grew faint and hazy. Night came like the turning out of a lamp, and in another moment tomorrow came.\n\nThe days and nights flickered by like the flapping of a black wing. The sun hopped across the sky in a bright arch, and the moon danced through its phases in seconds.\n\nI saw tall buildings rise like vapor and disappear. Lush forests surged up and faded into golden plains. The dial on the dashboard spun dizzyingly until it registered the year 802,701 A.D.! With an abrupt jerk, I pulled the brake, and the machine tumbled onto a soft lawn of white flowers."
                    ),
                    BookPageEntity(
                        bookId = "time_machine",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: The Golden Age of Eloi",
                        content = "A group of delicate, graceful little people clad in soft tunics gathered around me. They had large mild eyes and curly hair, speaking in a melodious, cooing tongue.\n\nThey crowned me with flowers and led me into a gigantic palace of marble and stained glass, where we feasted on delicious futuristic fruits.\n\nAt first, I believed Earth had become an effortless paradise, free from labor, disease, and war. But then I noticed that these lovely creatures—the Eloi—possessed the intellect of gentle children and lived in constant, unspoken dread of the dark."
                    ),
                    BookPageEntity(
                        bookId = "time_machine",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The Underworld Morlocks",
                        content = "My Time Machine had vanished from the lawn! In the morning dew, I discovered tracks leading to the bronze pedestal of the great White Sphinx.\n\nDeep below the ground, through dark ventilation shafts, thumped the rhythm of massive mechanical engines.\n\nI descended into the dark with a box of matches. In the subterranean shadows lurked the Morlocks—pale, ape-like creatures with glowing lidless eyes who maintained the machinery of the planet and preyed upon the defenseless surface people in the moonless nights!"
                    ),
                    BookPageEntity(
                        bookId = "time_machine",
                        pageNumber = 5,
                        chapterTitle = "Chapter 5: Escape to the Present",
                        content = "Surrounded by Morlocks, I struck a match! The burst of fire blinded them, buying me precious seconds to sprint to the Sphinx's bronze doors.\n\nI leaped into the saddle of the machine, reattached the control levers, and pulled back with all my might!\n\nThe subterranean horrors melted into streaks of light. The centuries unrolled in reverse. The sun raced backwards across the sky until the familiar walls of my London laboratory solidified around me once more.\n\nThere on the dining table lay two strange, withered white flowers given to me by Weena, the little Eloi friend I had saved—silent proof that even when intellect and civilizations crumble, love and mutual kindness endure forever."
                    )
                )
            )
        )

        // 9. A Journey to the Centre of the Earth (Sci-Fi, Large, 100 coins, 50 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "centre_earth",
                    title = "A Journey to the Centre of the Earth",
                    author = "Jules Verne",
                    description = "An eccentric professor, his nephew Axel, and an Icelandic guide descend into an extinct volcano to explore the primordial subterranean world.",
                    categoryName = BookCategory.SCIENCE_FICTION.name,
                    genre = "Planetary Adventure",
                    license = "Public Domain (Project Gutenberg #18857)",
                    source = "https://www.gutenberg.org/ebooks/18857",
                    bookSize = BookSize.LARGE.name,
                    coinPrice = 100,
                    completionReward = 50,
                    coverColorHex = 0xFF0D9488,
                    coverIconName = "Explore",
                    totalPages = 5,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "centre_earth",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: The Cryptic Parchment",
                        content = "On the 24th of May, 1863, my uncle, Professor Otto Lidenbrock, rushed into his little house in Hamburg holding an ancient runic manuscript by Snorri Sturluson.\n\nOut fluttered a dirty scrap of parchment covered in cryptic cipher!\n\nFor two days my uncle refused to eat or sleep until we deciphered the Latin code: 'Descend into the crater of Sneffels volcano before the calends of July, bold traveler, and you will reach the center of the earth. Arne Saknussemm.'\n\n'Pack our bags, Axel!' cried my uncle with blazing enthusiasm. 'Tomorrow we sail for Iceland!'"
                    ),
                    BookPageEntity(
                        bookId = "centre_earth",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Volcano's Throat",
                        content = "Accompanied by Hans Bjelke, a stalwart Icelandic eiderdown hunter of few words and unflinching courage, we climbed the snow-clad slopes of Mount Sneffels.\n\nOn the 28th of June, the shadow of Scartaris fell precisely upon the middle chimney of the crater.\n\nWe tied our ropes and descended into the black abyss. Deep beneath the basalt crust, the air grew warm and dense. Crystals of quartz and pyrite sparkled on the cavern walls like chandeliers in our electric lanterns."
                    ),
                    BookPageEntity(
                        bookId = "centre_earth",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: The Subterranean Sea",
                        content = "After days of marching through vast granite corridors, the sound of roaring waves echoed in the distance.\n\nWe emerged upon the shore of an immense underground ocean! Above us arched a granite vault glowing with continuous electrical luminescence like a subterranean aurora borealis.\n\nAround the beach grew forests of gigantic mushrooms thirty feet high, alongside giant ferns that had vanished from the earth's surface millions of years ago.\n\nHans constructed a sturdy raft of petrified wood, and we set sail across the Central Sea into the unknown."
                    ),
                    BookPageEntity(
                        bookId = "centre_earth",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: Duel of the Titans",
                        content = "Suddenly, forty fathoms beneath our raft, the waters churned with terrifying violence!\n\nAn enormous creature with the head of a lizard and teeth of a crocodile breached the surface—an Ichthyosaurus! Behind it rose a Plesiosaurus with a long serpent neck twisting in coils.\n\nThe two prehistoric monsters engaged in a titanic battle, hurling mountainous waves against our fragile raft. For two hours the duel raged until the serpent beast sank lifeless into the foaming depths."
                    ),
                    BookPageEntity(
                        bookId = "centre_earth",
                        pageNumber = 5,
                        chapterTitle = "Chapter 5: The Volcanic Geyser Home",
                        content = "At the far end of the sea, our route was blocked by a colossal granite wall bearing the carved initials 'A.S.'—Arne Saknussemm himself!\n\nWe packed gunpowder to blow open the passage. But the explosion cracked the bottom of the cavern, and the ocean rushed into a roaring vortex, carrying our raft downwards into a fiery vertical shaft!\n\nThe temperature soared. Water turned to scalding steam, and molten lava began to rise beneath our raft. We were inside an active volcanic vent!\n\nWith an earth-shattering roar, the eruption shot us upwards into blinding sunlight. When we opened our eyes, the raft rested on the sunny slopes of Mount Stromboli in Sicily! We had entered Iceland and emerged in the Mediterranean, conquering the heart of our planet."
                    )
                )
            )
        )

        // 10. Fifty Famous Stories Retold (History, Medium, 50 coins, 15 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "famous_stories",
                    title = "Fifty Famous Stories Retold",
                    author = "James Baldwin",
                    description = "Inspirational legends and historical tales of bravery, honesty, and wisdom, from King Alfred to William Tell and the Sword of Damocles.",
                    categoryName = BookCategory.HISTORY.name,
                    genre = "Historical Legends",
                    license = "Public Domain (Project Gutenberg #10745)",
                    source = "https://www.gutenberg.org/ebooks/10745",
                    bookSize = BookSize.MEDIUM.name,
                    coinPrice = 50,
                    completionReward = 15,
                    coverColorHex = 0xFFD97706,
                    coverIconName = "AccountBalance",
                    totalPages = 4,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "famous_stories",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: King Alfred and the Cakes",
                        content = "Many hundreds of years ago, there lived in England a wise and good king named Alfred. But fierce invaders came and drove Alfred into the lonely forests.\n\nOne day, tired and disguised as a common woodsman, Alfred rested in a humble cottage. The woodcutter's wife was baking oat cakes on the hearth.\n\n'Watch these cakes, good fellow,' she ordered, 'while I go milk the cow. Do not let them burn!'\n\nAlfred, deep in thought about his troubled country and planning how to bring peace, forgot all about the cakes. When the woman returned, they were burned to a cinder!\n\n'You lazy loon!' she scolded, not knowing he was king. 'You are ready enough to eat them, but too foolish to turn them!' When her husband arrived and bowed before King Alfred, the poor woman was terrified. But Alfred laughed kindly: 'You were right, good mother. A king who cannot mind the cakes must learn humility before he can rule his people.'"
                    ),
                    BookPageEntity(
                        bookId = "famous_stories",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Sword of Damocles",
                        content = "In ancient Sicily ruled Dionysius, a king of fabulous riches and absolute power. His courtier Damocles constantly flattered him: 'How fortunate you are! There is no one on earth as happy as you.'\n\n'Would you like to experience my happiness for a single day?' asked the King.\n\nDamocles rejoiced. He was dressed in royal robes, seated upon a golden throne, and served the rarest delicacies by scores of attendants.\n\nIn the midst of the banquet, Damocles looked up. Directly above his head hung a gleaming, razor-sharp sword, suspended from the ceiling by a single horsehair!\n\nHis smile froze. He could no longer taste the wine nor enjoy the music. 'Take me away!' he begged. 'I see now that great power and wealth are accompanied by constant peril!'"
                    ),
                    BookPageEntity(
                        bookId = "famous_stories",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: William Tell and the Apple",
                        content = "In Switzerland, the cruel Austrian governor Gessler set his hat upon a pole in the marketplace of Altdorf and commanded that every citizen must bow before it.\n\nWilliam Tell, a renowned hunter with a noble heart, walked past with his son Walter and refused to bow to a tyrant's hat.\n\nFurious, Gessler devised a wicked punishment: 'Since you are such a famed marksman, you shall shoot an apple from your son's head at eighty paces. If you miss, both you and the boy shall die!'\n\nThe boy stood brave and still. Tell drew his bowstring, aimed with unwavering precision, and loosed the arrow. It split the apple cleanly in two without touching a hair of the boy's head!\n\nWhen Gessler noticed a second arrow tucked in Tell's belt, he demanded to know why. 'If my first arrow had hurt my son,' replied Tell boldly, 'this second arrow was aimed straight for your heart!' That spark ignited the freedom of Switzerland."
                    ),
                    BookPageEntity(
                        bookId = "famous_stories",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The Endless Tale & Lessons of Time",
                        content = "There was once a king who loved stories so much that he offered his daughter in marriage to anyone who could tell an endless tale, but warned that if the story ended, the teller would lose his head.\n\nMany tried and failed, until a clever storyteller arrived and began: 'A king built a colossal granary and filled it with wheat. But there was a hole so small that only one locust could squeeze through at a time. Then a locust went in and carried away a grain of wheat. Then another locust went in and carried away a grain of wheat...'\n\nFor three months he repeated the phrase! At last the king cried in despair: 'Enough! Take my daughter and rule the kingdom, for your locusts will never finish their harvest!'\n\nThrough every tale of history, courage, cleverness, and truth outlast empires, teaching young readers that ideas shape the world."
                    )
                )
            )
        )

        // 11. The Chemical History of a Candle (Science, Small, 25 coins, 5 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "candle_science",
                    title = "The Chemical History of a Candle",
                    author = "Michael Faraday",
                    description = "Discover the magical physics and chemistry of fire, oxygen, combustion, and water through Michael Faraday's legendary Royal Institution lectures.",
                    categoryName = BookCategory.SCIENCE.name,
                    genre = "Popular Science",
                    license = "Public Domain (Project Gutenberg #14474)",
                    source = "https://www.gutenberg.org/ebooks/14474",
                    bookSize = BookSize.SMALL.name,
                    coinPrice = 25,
                    completionReward = 5,
                    coverColorHex = 0xFF059669,
                    coverIconName = "Science",
                    totalPages = 4,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "candle_science",
                        pageNumber = 1,
                        chapterTitle = "Lecture 1: The Anatomy of a Flame",
                        content = "There is not a higher law of nature governed by the universe that is not illustrated by the phenomena of a burning candle.\n\nLook at this modest candle. Why does it burn with such steady grace?\n\nThe heat of the match melts the wax at the top into a little liquid cup. The capillary action of the cotton wick draws the melted wax upwards, exactly as a plant draws water through its roots.\n\nWhen the liquid wax reaches the hot zone of the flame, it vaporizes into an invisible gas. It is not the solid wax or the liquid wax that burns—it is the wax vaporized into gas!"
                    ),
                    BookPageEntity(
                        bookId = "candle_science",
                        pageNumber = 2,
                        chapterTitle = "Lecture 2: The Zone of Mystery",
                        content = "If you look closely at the flame, you will see it is not a solid mass of fire.\n\nAt the center, right around the wick, is a dark shadow. That is the coolest part of the flame, containing pure, unburned wax vapor waiting for oxygen!\n\nIf we place a small glass tube directly into that dark center, we can lead the vapor away and light it at the other end of the tube like a gas lamp.\n\nThe bright luminous part above contains billions of microscopic carbon particles heated white-hot by combustion. Their glow gives the candle its warm, cheerful light."
                    ),
                    BookPageEntity(
                        bookId = "candle_science",
                        pageNumber = 3,
                        chapterTitle = "Lecture 3: What Fire Leaves Behind",
                        content = "What happens to the wax when it burns away? Does it disappear from existence?\n\nNature never destroys matter; it only transforms it.\n\nIf we hold a cold, clean glass spoon over the flame, we observe tiny droplets of clear liquid condensing upon the surface. What is that liquid? Pure water!\n\nThe hydrogen in the wax joins with the oxygen of the atmosphere to create water vapor. Fire literally creates water as it burns!"
                    ),
                    BookPageEntity(
                        bookId = "candle_science",
                        pageNumber = 4,
                        chapterTitle = "Lecture 4: Respiration and the Light Within",
                        content = "The other product of the candle is carbon dioxide, formed when glowing carbon atoms combine with oxygen.\n\nHere is the most wonderful truth in all of science: our very bodies are gentle candles! When we breathe in oxygen, it combines with the fuel of our food, producing warmth, energy, and carbon dioxide.\n\nIn every human being, the flame of life burns steadily and brightly. As you grow and explore the wonders of science, let your mind shine as a light to all who seek truth."
                    )
                )
            )
        )

        // 12. The Story-Book of Science (Science, Medium, 50 coins, 15 reward)
        list.add(
            SeedBook(
                entity = BookEntity(
                    id = "storybook_science",
                    title = "The Story-Book of Science",
                    author = "Jean-Henri Fabre",
                    description = "Uncle Paul explains the incredible marvels of nature: how ants farm, how clouds float, why bees build hexagons, and where thunder comes from.",
                    categoryName = BookCategory.SCIENCE.name,
                    genre = "Nature & Discovery",
                    license = "Public Domain (Project Gutenberg #29107)",
                    source = "https://www.gutenberg.org/ebooks/29107",
                    bookSize = BookSize.MEDIUM.name,
                    coinPrice = 50,
                    completionReward = 15,
                    coverColorHex = 0xFF0284C7,
                    coverIconName = "Biotech",
                    totalPages = 4,
                    isUnlockedByDefault = false
                ),
                pages = listOf(
                    BookPageEntity(
                        bookId = "storybook_science",
                        pageNumber = 1,
                        chapterTitle = "Chapter 1: The Ant's Dairy Cattle",
                        content = "Under the shade of the hazel trees, Uncle Paul sat with Jules and Emile to watch a procession of black ants climbing a rose bush.\n\n'Look closely at those green plant lice on the stems,' said Uncle Paul. 'Those aphids are the ant's cows!'\n\nThe boys laughed in disbelief. 'Cows? Those tiny green bugs?'\n\n'Indeed,' Uncle Paul smiled. 'The aphids drink the sweet sap of the rose. When an ant strokes an aphid gently with its antennae, the aphid secretes a droplet of sugary honeydew. The ants guard their aphids like shepherds, shelter them in winter, and milk them every summer day!'"
                    ),
                    BookPageEntity(
                        bookId = "storybook_science",
                        pageNumber = 2,
                        chapterTitle = "Chapter 2: The Hexagon of the Honeybee",
                        content = "Next, Uncle Paul brought out an empty honeycomb from the orchard apiary.\n\n'Why are all the cells regular six-sided hexagons?' he asked.\n\n'Why not circles or squares?' asked Jules.\n\n'Circles would leave wasteful empty gaps between them,' explained Uncle Paul. 'Triangles and squares could tile the plane without gaps, but the hexagon encloses the maximum volume of honey with the minimum amount of beeswax perimeter. Through millions of years of natural geometry, the little bee builds with the mathematical perfection of an engineer!'"
                    ),
                    BookPageEntity(
                        bookId = "storybook_science",
                        pageNumber = 3,
                        chapterTitle = "Chapter 3: The Secret of Clouds and Thunder",
                        content = "A summer storm swept over the distant mountains. Lightning flashed in jagged violet ribbons across the purple sky.\n\n'Count the seconds until the rumble,' instructed Uncle Paul.\n\n'One... two... three... boom!' counted Emile.\n\n'Light travels at three hundred thousand kilometers per second, almost instantaneously to our eyes,' Uncle Paul said. 'Sound travels at only three hundred and forty meters per second. Since three seconds elapsed, the lightning struck one kilometer away. The flash and the thunder were born together in the same breath!'"
                    ),
                    BookPageEntity(
                        bookId = "storybook_science",
                        pageNumber = 4,
                        chapterTitle = "Chapter 4: The Wonders of Curiosity",
                        content = "'Nature is an endless book of enchantments,' concluded Uncle Paul as the rainbow arched across the evening meadow.\n\n'Every stone, every leaf, and every star above has a marvelous story to tell if you only open your eyes and ask: Why?'\n\nThe boys hugged their uncle and gazed up at the emerging constellations, their hearts burning with curiosity to explore every secret of the universe."
                    )
                )
            )
        )

        return list
    }
}
