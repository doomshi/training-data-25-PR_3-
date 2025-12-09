import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Клас BasicDataOperationUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними Map.</li>
 *   <li>{@link #findByKey()} - Здійснює пошук елемента за ключем в Map.</li>
 *   <li>{@link #findByValue()} - Здійснює пошук елемента за значенням в Map.</li>
 *   <li>{@link #addEntry()} - Додає новий запис до Map.</li>
 *   <li>{@link #removeByKey()} - Видаляє запис з Map за ключем.</li>
 *   <li>{@link #removeByValue()} - Видаляє записи з Map за значенням.</li>
 *   <li>{@link #sortByKey()} - Сортує Map за ключами.</li>
 *   <li>{@link #sortByValue()} - Сортує Map за значеннями.</li>
 * </ul>
 */
public class BasicDataOperationUsingMap {
    private final Tortoise KEY_TO_SEARCH_AND_DELETE = new Tortoise("Броня", "shellThickness=3.1");
    private final Tortoise KEY_TO_ADD = new Tortoise("Казка", "shellThickness=3.3");

    private final String VALUE_TO_SEARCH_AND_DELETE = "Микола";
    private final String VALUE_TO_ADD = "Аркадій";

    private HashMap<Tortoise, String> HashMap;
    private LinkedHashMap<Tortoise, String> LinkedHashMap;

    /**
     * Компаратор для сортування Map.Entry за значеннями String.
     * Використовує метод String.compareTo() для порівняння імен власників.
     */
    static class OwnerValueComparator implements Comparator<Map.Entry<Tortoise, String>> {
        @Override
        public int compare(Map.Entry<Tortoise, String> e1, Map.Entry<Tortoise, String> e2) {
            String v1 = e1.getValue();
            String v2 = e2.getValue();
            if (v1 == null && v2 == null) return 0;
            if (v1 == null) return -1;
            if (v2 == null) return 1;
            return v1.compareTo(v2);
        }
    }

    /**
     * Внутрішній клас Pet для зберігання інформації про домашню тварину.
     * 
     * Реалізує Comparable<Pet> для визначення природного порядку сортування.
     * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за видом (species) за спаданням.
     */
    public static class Tortoise implements Comparable<Tortoise> {
        private final String nickname;
        private final String species;

        public Tortoise(String nickname) {
            this.nickname = nickname;
            this.species = null;
        }

        public Tortoise(String nickname, String species) {
            this.nickname = nickname;
            this.species = species;
        }

        public String getNickname() { 
            return nickname; 
        }

        public String getSpecies() {
            return species;
        }

        /**
         * Порівнює цей об'єкт Pet з іншим для визначення порядку сортування.
         * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за видом (species) за спаданням.
         * 
         * @param other Pet об'єкт для порівняння
         * @return негативне число, якщо цей Pet < other; 
         *         0, якщо цей Pet == other; 
         *         позитивне число, якщо цей Pet > other
         * 
         * Критерій порівняння: поля nickname (кличка) за зростанням та species (вид) за спаданням.
         * 
         * Цей метод використовується:
         * - TreeMap для автоматичного сортування ключів Pet за nickname (зростання), потім за species (спадання)
         * - Collections.sort() для сортування Map.Entry за ключами Pet
         * - Collections.binarySearch() для пошуку в відсортованих колекціях
         */
        @Override
        public int compareTo(Tortoise other) {
            if (other == null) return 1;

            // Кличка (nickname) — за зростанням
            int nicknameComparison = 0;
            if (this.nickname == null && other.nickname == null) {
                nicknameComparison = 0;
            } else if (this.nickname == null) {
                nicknameComparison = -1;
            } else if (other.nickname == null) {
                nicknameComparison = 1;
            } else {
                nicknameComparison = this.nickname.compareTo(other.nickname);
            }

            if (nicknameComparison != 0) {
                return nicknameComparison;
            }

            // Товщина панциря (shellThickness) — за зростанням
            double thicknessThis = parseShellThickness(this.species);
            double thicknessOther = parseShellThickness(other.species);
            return Double.compare(thicknessThis, thicknessOther);
        }

        /**
         * Парсить значення shellThickness з рядка species типу "shellThickness=3.1".
         * Якщо не вдалося розпарсити, повертає Double.MIN_VALUE.
         */
        private static double parseShellThickness(String species) {
            if (species == null) return Double.MIN_VALUE;
            try {
                int idx = species.indexOf("shellThickness=");
                if (idx >= 0) {
                    String num = species.substring(idx + "shellThickness=".length());
                    return Double.parseDouble(num);
                }
            } catch (Exception e) {
                // ігноруємо помилки парсингу
            }
            return Double.MIN_VALUE;
        }

        /**
         * Перевіряє рівність цього Pet з іншим об'єктом.
         * Два Pet вважаються рівними, якщо їх клички (nickname) та види (species) однакові.
         * 
         * @param obj об'єкт для порівняння
         * @return true, якщо об'єкти рівні; false в іншому випадку
         * 
         * Критерій рівності: поля nickname (кличка) та species (вид).
         * 
         * Важливо: метод узгоджений з compareTo() - якщо equals() повертає true,
         * то compareTo() повертає 0, оскільки обидва методи порівнюють за nickname та species.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Tortoise other = (Tortoise) obj;

            boolean nicknameEquals = nickname != null ? nickname.equals(other.nickname) : other.nickname == null;
            boolean speciesEquals = species != null ? species.equals(other.species) : other.species == null;

            return nicknameEquals && speciesEquals;
        }

        /**
         * Повертає хеш-код для цього Pet.
         * 
         * @return хеш-код, обчислений на основі nickname та species
         * 
         * Базується на полях nickname та species для узгодженості з equals().
         * 
         * Важливо: узгоджений з equals() - якщо два Pet рівні за equals()
         * (мають однакові nickname та species), вони матимуть однаковий hashCode().
         */
        @Override
        public int hashCode() {
            // Початкове значення: хеш-код поля nickname (або 0, якщо nickname == null)
            int result = nickname != null ? nickname.hashCode() : 0;
            
            // Комбінуємо хеш-коди полів за формулою: result = 31 * result + hashCode(поле)
            // Множник 31 - просте число, яке дає хороше розподілення хеш-кодів
            // і оптимізується JVM як (result << 5) - result
            // Додаємо хеш-код виду (або 0, якщо species == null) до загального результату
            result = 31 * result + (species != null ? species.hashCode() : 0);
            
            return result;
        }

        /**
         * Повертає строкове представлення Pet.
         * 
         * @return кличка тварини (nickname), вид (species) та hashCode
         */
        @Override
        public String toString() {
            if (species != null) {
                return "Tortoise{nickname='" + nickname + "', species='" + species + "', hashCode=" + hashCode() + "}";
            }
            return "Tortoise{nickname='" + nickname + "', hashCode=" + hashCode() + "}";
        }
    }

    /**
     * Конструктор, який ініціалізує об'єкт з готовими даними.
     * 
     * @param hashtable Hashtable з початковими даними (ключ: Pet, значення: ім'я власника)
     * @param treeMap TreeMap з початковими даними (ключ: Pet, значення: ім'я власника)
     */
    BasicDataOperationUsingMap(HashMap<Tortoise, String> HashMap, LinkedHashMap<Tortoise, String> LinkedHashMap) {
        this.HashMap = HashMap;
        this.LinkedHashMap = LinkedHashMap;
    }
    
    /**
     * Виконує комплексні операції з Map.
     * 
     * Метод виконує різноманітні операції з Map: пошук, додавання, видалення та сортування.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з Hashtable
        System.out.println("========= Операції з HashMap =========");
        System.out.println("Початковий розмір HashMap: " + HashMap.size());
        
        // Пошук до сортування
        findByKeyInHashMap();
        findByValueInHashMap();

        printHashMap();
        sortHashMap();
        printHashMap();

        // Пошук після сортування
        findByKeyInHashMap();
        findByValueInHashMap();

        addEntryToHashMap();
        
        removeByKeyFromHashMap();
        removeByValueFromHashMap();
               
        System.out.println("Кінцевий розмір HashMap: " + HashMap.size());

        // Потім обробляємо TreeMap
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        System.out.println("Початковий розмір LinkedHashMap: " + LinkedHashMap.size());
        
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        printLinkedHashMap();
        // Сортування LinkedHashMap за ключами (природний порядок Tortoise)
        sortLinkedHashMap();
        printLinkedHashMap();

        addEntryToLinkedHashMap();
        
        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();
        
        System.out.println("Кінцевий розмір LinkedHashMap: " + LinkedHashMap.size());
    }


    // ===== Методи для Hashtable =====

    /**
     * Виводить вміст Hashtable без сортування.
     * Hashtable не гарантує жодного порядку елементів.
     */
    private void printHashMap() {
        System.out.println("\n=== Пари ключ-значення в HashMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Tortoise, String> entry : HashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пари ключ-значення в HashMap");
    }

    /**
     * Сортує Hashtable за ключами.
     * Використовує Collections.sort() з природним порядком Pet (Pet.compareTo()).
     * Перезаписує hashtable відсортованими даними.
     */
    private void sortHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Pet
        LinkedList<Tortoise> sortedKeys = new LinkedList<>(HashMap.keySet());
        Collections.sort(sortedKeys);
        
        // Створюємо нову Hashtable з відсортованими ключами
        HashMap<Tortoise, String> sortedHashMap = new HashMap<>();
        for (Tortoise key : sortedKeys) {
            sortedHashMap.put(key, HashMap.get(key));
        }
        
        // Перезаписуємо оригінальну hashtable
        HashMap = sortedHashMap;

        PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
    }

    /**
     * Сортує LinkedHashMap за ключами за природним порядком Tortoise.
     * Перезаписує LinkedHashMap відсортованими даними.
     */
    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        LinkedList<Tortoise> sortedKeys = new LinkedList<>(LinkedHashMap.keySet());
        Collections.sort(sortedKeys);

        LinkedHashMap<Tortoise, String> sorted = new LinkedHashMap<>();
        for (Tortoise key : sortedKeys) {
            sorted.put(key, LinkedHashMap.get(key));
        }

        LinkedHashMap = sorted;

        PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в Hashtable.
     * Використовує Pet.hashCode() та Pet.equals() для пошуку.
     */
    void findByKeyInHashMap() {
        long timeStart = System.nanoTime();

        boolean found = HashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

        if (found) {
            String value = HashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в Hashtable.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список Entry та сортуємо за значеннями
        LinkedList<Map.Entry<Tortoise, String>> entries = new LinkedList<>(HashMap.entrySet());
        OwnerValueComparator comparator = new OwnerValueComparator();
        Collections.sort(entries, comparator);

        // Створюємо тимчасовий Entry для пошуку
        Map.Entry<Tortoise, String> searchEntry = new Map.Entry<Tortoise, String>() {
            public Tortoise getKey() { return null; }
            public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
            public String setValue(String value) { return null; }
        };

        int position = Collections.binarySearch(entries, searchEntry, comparator);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в HashMap");

        if (position >= 0) {
            Map.Entry<Tortoise, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Tortoise: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    /**
     * Додає новий запис до Hashtable.
     */
    void addEntryToHashMap() {
        long timeStart = System.nanoTime();

        HashMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до HashMap");

        System.out.println("Додано новий запис: Tortoise='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з Hashtable за ключем.
     */
    void removeByKeyFromHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = HashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з Hashtable за значенням.
     */
    void removeByValueFromHashMap() {
        long timeStart = System.nanoTime();

        LinkedList<Tortoise> keysToRemove = new LinkedList<>();
        for (Map.Entry<Tortoise, String> entry : HashMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
                keysToRemove.add(entry.getKey());
            }
        }
        
        for (Tortoise key : keysToRemove) {
            HashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з HashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для TreeMap =====

    /**
     * Виводить вміст TreeMap.
     * TreeMap автоматично відсортована за ключами (Pet nickname за зростанням, species за спаданням).
     */
    private void printLinkedHashMap() {
        System.out.println("\n=== Пари ключ-значення в LinkedHashMap ===");

        long timeStart = System.nanoTime();
        for (Map.Entry<Tortoise, String> entry : LinkedHashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в TreeMap");
    }

    /**
     * Здійснює пошук елемента за ключем в TreeMap.
     * Використовує Pet.compareTo() для навігації по дереву.
     */
    void findByKeyInLinkedHashMap() {
        long timeStart = System.nanoTime();

        boolean found = LinkedHashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

        if (found) {
            String value = LinkedHashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в TreeMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список Entry та сортуємо за значеннями
        LinkedList<Map.Entry<Tortoise, String>> entries = new LinkedList<>(LinkedHashMap.entrySet());
        OwnerValueComparator comparator = new OwnerValueComparator();
        Collections.sort(entries, comparator);

        // Створюємо тимчасовий Entry для пошуку
        Map.Entry<Tortoise, String> searchEntry = new Map.Entry<Tortoise, String>() {
            public Tortoise getKey() { return null; }
            public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
            public String setValue(String value) { return null; }
        };

        int position = Collections.binarySearch(entries, searchEntry, comparator);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в LinkedHashMap");

        if (position >= 0) {
            Map.Entry<Tortoise, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Tortoise: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    /**
     * Додає новий запис до TreeMap.
     */
    void addEntryToLinkedHashMap() {
        long timeStart = System.nanoTime();

        LinkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до LinkedHashMap");

        System.out.println("Додано новий запис: Tortoise='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з TreeMap за ключем.
     */
    void removeByKeyFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = LinkedHashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з LinkedHashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з TreeMap за значенням.
     */
    void removeByValueFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        LinkedList<Tortoise> keysToRemove = new LinkedList<>();
        for (Map.Entry<Tortoise, String> entry : LinkedHashMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
                keysToRemove.add(entry.getKey());
            }
        }
        
        for (Tortoise key : keysToRemove) {
            LinkedHashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з LinkedHashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    /**
     * Головний метод для запуску програми.
     */
    public static void main(String[] args) {
        // Створюємо початкові дані (ключ: Pet, значення: ім'я власника)
        HashMap<Tortoise, String> HashMap = new HashMap<>();
        HashMap.put(new Tortoise("Атлант", "shellThickness=2.5"), "Руслан");
        HashMap.put(new Tortoise("Броня", "shellThickness=3.1"), "Олеся");
        HashMap.put(new Tortoise("Вічність", "shellThickness=4.2"), "Микола");
        HashMap.put(new Tortoise("Гном", "shellThickness=1.8"), "Аліна");
        HashMap.put(new Tortoise("Броня", "shellThickness=2.9"), "Тимур");
        HashMap.put(new Tortoise("Дзвін", "shellThickness=3.7"), "Микола");
        HashMap.put(new Tortoise("Еон", "shellThickness=4.5"), "Софія");
        HashMap.put(new Tortoise("Жук", "shellThickness=2.2"), "Віталій");
        HashMap.put(new Tortoise("Зевс", "shellThickness=3.9"), "Олеся");
        HashMap.put(new Tortoise("Ікар", "shellThickness=2.7"), "Надія");

        LinkedHashMap<Tortoise, String> LinkedHashMap = new LinkedHashMap<Tortoise, String>() {{
            put(new Tortoise("Атлант", "shellThickness=2.5"), "Руслан");
            put(new Tortoise("Броня", "shellThickness=3.1"), "Олеся");
            put(new Tortoise("Вічність", "shellThickness=4.2"), "Микола");
            put(new Tortoise("Гном", "shellThickness=1.8"), "Аліна");
            put(new Tortoise("Броня", "shellThickness=2.9"), "Тимур");
            put(new Tortoise("Дзвін", "shellThickness=3.7"), "Микола");
            put(new Tortoise("Еон", "shellThickness=4.5"), "Софія");
            put(new Tortoise("Жук", "shellThickness=2.2"), "Віталій");
            put(new Tortoise("Зевс", "shellThickness=3.9"), "Олеся");
            put(new Tortoise("Ікар", "shellThickness=2.7"), "Надія");
        }};

        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationUsingMap operations = new BasicDataOperationUsingMap(HashMap, LinkedHashMap);
        operations.executeDataOperations();
    }
}

