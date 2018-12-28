package cc.origind.server.util;

import cc.origind.server.update.Change;

import java.util.*;

public interface ChangeUtil {
    static Set<Change> merge(Collection<Change> changes) {
        List<Change> merged = mergeUpgrade(changes);
        List<Change> set = new ArrayList<>(merged);
        List<Change> temp = new ArrayList<>();
        for (Change change : merged) {
            if (change.isDelete())
                set.removeIf(c -> {
                    if (c.isAdd())
                        return Objects.equals(c.getFile(), change.getFile());
                    else if (c.isUpgrade()) {
                        if (Objects.equals(c.getTo(), change.getFile()))
                            return true;
                        else if (Objects.equals(c.getFile(), change.getFile())) {
                            temp.add(new Change("add", c.getTo()));
                            return true;
                        }
                    } else if (c.isDelete()) {
                        return !Objects.equals(c.getFile(), change.getFile());
                    }
                    return false;
                });
        }
        set.addAll(temp);
        set.removeIf(change -> change.isUpgrade() && Objects.isNull(change.getTo()));
        return new HashSet<>(set);
    }

    static List<Change> mergeUpgrade(Collection<Change> changes) {
        List<Change> result = new LinkedList<>();
        upper:
        for (Change change : changes) {
            for (Change target : result) {
                if (change.getFile().equals(target.getTo())) {
                    target.setTo(change.getTo());
                    continue upper;
                }
            }
            result.add(change);
        }
        return result;
    }

    static boolean canMerge(Change previous, Change next) {
        return previous.isUpgrade() && next.isUpgrade() && previous.getTo().equals(next.getFile());
    }

    static Change mergeUpgrade(Change previous, Change next) {
        if (previous.isUpgrade() && next.isUpgrade())
            if (previous.getTo().equals(next.getFile())) {
                next.setFile(previous.getFile());
                return next;
            }
        return null;
    }
}
