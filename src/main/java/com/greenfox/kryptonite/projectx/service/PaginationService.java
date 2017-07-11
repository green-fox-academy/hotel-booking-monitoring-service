package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginationService {

  final String HOST = "https://greenfox-kryptonite.herokuapp.com/pageviews?page=";
  final int TWENTY = 20;

  ArrayList<EventToDatabase> pagination(EventToDatabaseRepository repo, int page) {
    ArrayList<EventToDatabase> finalList = new ArrayList<>();
    ArrayList<EventToDatabase> allEventList = (ArrayList<EventToDatabase>) repo.findAllByOrderByIdAsc();

    int endIndex = checkEndIndex(page, allEventList);

    if ((page != 0) && (allEventList.size() > TWENTY)) {
      for (int i = page * TWENTY - TWENTY; i < endIndex; ++i) {
        finalList.add(allEventList.get(i));
      }
    } else {
      finalList = allEventList;
    }
    return finalList;
  }

  int checkEndIndex(int page, ArrayList<EventToDatabase> allEventList) {
    return (allEventList.size() < page * TWENTY) ? allEventList.size() : page * TWENTY;
  }

  String checkNextPage(String self, String last, int page) {
    return self.equals(last) ? "this is the last page" : HOST + (page + 1);
  }

  String checkPrevPage(int page) {
    return HOST + (page - 1);
  }
}
