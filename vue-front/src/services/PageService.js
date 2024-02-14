class PageService {
    router

    constructor(router) {
        this.router = router;
    }

    gotoLoginPage() {
        this.router.push({ path: "/Login" });
    }

    gotoProfilePage() {
        this.router.push({ path: "/Profile" });
    }

    gotoLivretDetailsPage(id) {
        this.router.gotoLivretDetailsPage(id);
    }

    gotoLivretModifyPage(id) {
        this.router.gotoLivretModifyPage(id);
    }
}

export default PageService