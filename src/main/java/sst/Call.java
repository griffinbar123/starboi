package sst;

import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Call {
    @NonNull
    private Game game;
    public static int calls = 0;

    public void ExecCALL() {
        calls++;
    }
}
