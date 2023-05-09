package dtos;

import java.util.List;

public class ForumThreadsDTO {
    List<ForumThreadDTO> threads;

    public ForumThreadsDTO(List<ForumThreadDTO> threads) {
        this.threads = threads;
    }
}
