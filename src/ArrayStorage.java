import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        Arrays.fill(storage, null);
        size = 0;
    }

    void save(Resume r) {
        if (get(r.uuid) == null) {
            storage[size++] = r;
        } else {
            System.out.println("This UUID already exists");
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        int indexToDelete = -1;

        for (int i = 0; i < this.size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                indexToDelete = i;
                break;
            }
        }

        if (indexToDelete != -1) {
            System.arraycopy(storage, indexToDelete + 1, storage, indexToDelete, size - indexToDelete - 1);
            storage[--size] = null;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return this.size;
    }
}
