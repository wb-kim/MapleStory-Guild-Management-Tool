<template>
  <div class="wrapper">
    <side-bar>
      <template slot="links">
        <sidebar-link to="/mainpage" name="가입 규칙 및 공지" icon="ti-panel" />
        <sidebar-link to="/user-list" name="길드 전체 인원명단" icon="ti-user" />
        <sidebar-link
          to="/table-list"
          name="길드 무릉 리스트"
          icon="ti-view-list-alt"
        />
        <br>
        <sidebar-link to="/typography" name="노블 인원 관리" icon="ti-pencil-alt" style="margin-top: 3vw;"/>
        <sidebar-link to="/icons" name="20세기 인원 관리" icon="ti-pencil-alt" />
        <sidebar-link to="/maps" name="도탁스 인원 관리" icon="ti-pencil-alt" />
        <sidebar-link to="/notifications" name="인원 관리 필요 명단" icon="ti-check" />
        <sidebar-link to="/notifications" name="우수노블 정정 필요 명단" icon="ti-check" />
        <sidebar-link to="/notifications" name="로그 조회" icon="ti-list" />
      </template>
      <mobile-menu>
        <li class="nav-item">
          <a class="nav-link">
            <i class="ti-panel"></i>
            <p>Stats</p>
          </a>
        </li>
        <drop-down
          class="nav-item"
          title="5 Notifications"
          title-classes="nav-link"
          icon="ti-bell"
        >
          <a class="dropdown-item">Notification 1</a>
          <a class="dropdown-item">Notification 2</a>
          <a class="dropdown-item">Notification 3</a>
          <a class="dropdown-item">Notification 4</a>
          <a class="dropdown-item">Another notification</a>
        </drop-down>
        <li class="nav-item">
          <a class="nav-link">
            <i class="ti-settings"></i>
            <p>Settings</p>
          </a>
        </li>
        <li class="divider"></li>
      </mobile-menu>
    </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content @click.native="toggleSidebar"> </dashboard-content>

      <content-footer></content-footer>
    </div>
  </div>
</template>
<style lang="scss"></style>
<script>
import TopNavbar from "./TopNavbar.vue";
import ContentFooter from "./ContentFooter.vue";
import DashboardContent from "./Content.vue";
import MobileMenu from "./MobileMenu";
export default {
  components: {
    TopNavbar,
    ContentFooter,
    DashboardContent,
    MobileMenu,
  },
  methods: {
    toggleSidebar() {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false);
      }
    },
  },
};
</script>
