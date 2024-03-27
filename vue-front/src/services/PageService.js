class PageService {
    router

    constructor(router) {
        this.router = router;
    }

    refresh() {
        this.router.go();
    }

    gotoLoginPage() {
        this.router.push({ path: "/Login" });
    }

    gotoProfilePage() {
        this.router.push({ path: "/Profile" });
    }

    gotoLivretDetailsPage(id) {
        this.router.push({ path: `/Livret/${id}/details` });
    }

    gotoLivretModifyPage(id) {
        this.router.push({ path: `/Livret/${id}/modify` });
    }
}

export default PageService