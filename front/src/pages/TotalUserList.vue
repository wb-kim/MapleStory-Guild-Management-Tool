<template>
  <table-slot>
    <thead>
    <tr style="font-size:small; font-weight: bold;">
      <th>본캐</th>
      <th>부캐</th>
      <th>20세기 수플</th>
      <th>20세기 수로</th>
      <th colspan="3">20세기 자유</th>
      <th>도탁스 수플</th>
      <th colspan="4">도탁스 자유</th>
      <th>면제권</th>
    </tr>
    </thead>
    <tbody v-for="item in items">
    <tr style="font-size:small;" :style="{ backgroundColor: item.styleString }">
      <td> {{ item.main }} </td>
      <td> {{ item.sub }}</td>
      <td> {{ item.centurySP }} </td>
      <td> {{ item.centuryS }}</td>
      <td> {{ item.centuryUpper1 }}</td>
      <td> {{ item.centuryUpper2 }}</td>
      <td> {{ item.centuryUpper3 }}</td>
      <td> {{ item.dotax }}</td>
      <td> {{ item.dotaxUpper1 }}</td>
      <td> {{ item.dotaxUpper2 }}</td>
      <td> {{ item.dotaxUpper3 }}</td>
      <td> {{ item.dotaxUpper4 }}</td>
      <td> {{ item.exemptedDate }}</td>
    </tr>
    </tbody>
  </table-slot>
</template>
<script>
import { TableSlot } from "@/components";


export default {
  components: {
    "table-slot" : TableSlot,
  },
  data() {
    return {
      items: [],
    };
  },
  created() {
    this.getTotalList();
  },
  computed: {
    isAdmin(idx) {
      if (idx == 2) {
        return true;
      } else {
        return false;
      }
    }
  },
  methods: {
    getTotalList() {
      var vm = this;
      this.$axios
        .post("/Main/getTotalList", {
          searchStr: "",
        })
        .then(function (response) {
          console.log(response);
          var itemArray = [];
          for (var i = 0; i < response.data.length; i++) { 
            var styleString = "";
            if (response.data[i].admin == 2) {
              styleString = "#f4cccc";
            } else if (response.data[i].admin == 1) {
              styleString = "#efefef";
            } else {
              styleString = "";
            }
            var item = {
              styleString: styleString,
              admin: response.data[i].admin,
              main: response.data[i].main == null ? "" : response.data[i].main,
              sub: response.data[i].sub[0] == null ? "" : response.data[i].sub[0],
              centurySP: response.data[i].century[0] == null ? "" : response.data[i].century[0],
              centuryS: response.data[i].century[1] == null ? "" : response.data[i].century[1],
              centuryUpper1: response.data[i].century[2] == null ? "" : response.data[i].century[2],
              centuryUpper2: response.data[i].centuryUpper[0] == null ? "" : response.data[i].centuryUpper[0],
              centuryUpper3: response.data[i].centuryUpper[1] == null ? "" : response.data[i].centuryUpper[1],
              dotax: response.data[i].dotax[0] == null ? "" : response.data[i].dotax[0],
              dotaxUpper1: response.data[i].dotaxUpper[0] == null ? "" : response.data[i].dotaxUpper[0],
              dotaxUpper2: response.data[i].dotaxUpper[1] == null ? "" : response.data[i].dotaxUpper[1],
              dotaxUpper3: response.data[i].dotaxUpper[2] == null ? "" : response.data[i].dotaxUpper[2],
              dotaxUpper4: response.data[i].dotaxUpper[3] == null ? "" : response.data[i].dotaxUpper[3],
              exemptedDate: response.data[i].exemptedDate == null ? "" : response.data[i].exemptedDate,
              
            };
            itemArray.push(item);
          }

          vm.items = itemArray;
        });
    }
  }
};
</script>
<style>
</style>
