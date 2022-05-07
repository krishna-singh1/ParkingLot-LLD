package model;

import exception.InvalidCommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Command {
  private static final String SPACE = " ";
  private String commandName;
  private List<String> params;

  public String getCommandName() {
    return commandName;
  }

  public List<String> getParams() {
    return params;
  }

  public Command(final String inputLine) {
    final List<String> tokenList =
        Arrays.stream(inputLine.trim().split(SPACE))
            .map(String::trim)
            .filter(token -> (token.length() > 0))
            .collect(Collectors.toList());

    if (tokenList.size() == 0) {
      commandName ="";
      params = new ArrayList<>();
    } else {
      commandName = tokenList.get(0).toLowerCase();
      tokenList.remove(0);
      params = tokenList;
    }
  }
}
