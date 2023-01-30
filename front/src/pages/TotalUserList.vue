<template>
      <table-slot>
        <thead>
        <tr>
          <th>이름</th>
          <th colspan="2">타입</th>
          <th>클러스터 IP</th>
          <th>포트</th>
          <th>외부 IP</th>
          <th>생성시간</th>
          <th>레이블</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>asdf</td>
          <td>asdf</td>
          <td>asdf</td>
          <td>asfd</td>
          <td>asdf</td>
          <td>asdf</td>
          <td>{asdf</td>
          <td>asdf</td>
          <!--td>{{ item.label }}</td-->
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
    };
  },
};
</script>
<style></style>
